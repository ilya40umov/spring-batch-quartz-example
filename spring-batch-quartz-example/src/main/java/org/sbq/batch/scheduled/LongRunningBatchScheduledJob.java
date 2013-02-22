package org.sbq.batch.scheduled;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ilya40umov
 */
@DisallowConcurrentExecution
public class LongRunningBatchScheduledJob extends AbstractScheduledJob
{
    private final String jobName = "longRunningBatchJob";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("scheduledFireTime", new JobParameter(context.getScheduledFireTime()));
        runHandlingPossibleRecovery(new JobParameters(parameters));
    }

    private void runHandlingPossibleRecovery(JobParameters jobParameters) throws JobExecutionException
    {
        JobRegistry jobRegistry = getJobRegistry();
        JobLauncher jobLauncher = getJobLauncher();
        JobExplorer jobExplorer = getJobExplorer();
        JobRepository jobRepository = getJobRepository();
        try
        {
            // whenever Quartz performs a recovery process(it detects that a job was running when a server crashed and starts it again),
            // we need to deal with a previous job execution recorded by Spring Batch

            // since we have @DisallowConcurrentExecution on this job, all running instances are considered to be crashed/stuck ones.
            // in fact there can possibly be only one execution is running state
            Set<JobExecution> runningExecutions = jobExplorer.findRunningJobExecutions(jobName);
            for (JobExecution runningExecution : runningExecutions)
            {
                // jobOperator.stop() is for graceful scenario,
                // it's not enough for our case because the job is already crashed and won't stop even if we notify it
                runningExecution.setStatus(BatchStatus.STOPPED);
                runningExecution.setEndTime(new Date());
                jobRepository.update(runningExecution);
            }
            // now we presumably can run the job
            jobLauncher.run(jobRegistry.getJob(jobName), jobParameters);
        } catch (Exception e)
        {
            throw new JobExecutionException(e);
        }
    }

}
