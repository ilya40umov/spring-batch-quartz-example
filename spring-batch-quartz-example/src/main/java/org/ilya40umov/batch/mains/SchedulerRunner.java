/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ilya40umov.batch.mains;

import org.ilya40umov.batch.configurations.SchedulerRunnerConfiguration;
import org.ilya40umov.batch.scheduled.CalculateEventMetricsScheduledJob;
import org.ilya40umov.batch.scheduled.CalculateOnlineMetricsScheduledJob;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author ilya40umov
 */
public class SchedulerRunner
{
    private final ApplicationContext appCtx;
    private final Scheduler scheduler;

    public SchedulerRunner()
    {
        super();
        appCtx = new AnnotationConfigApplicationContext(SchedulerRunnerConfiguration.class);
        scheduler = appCtx.getBean(Scheduler.class);
    }

    public static void main(String[] args) throws SchedulerException
    {
        SchedulerRunner schedulerRunner = new SchedulerRunner();
        schedulerRunner.invoke();
    }

    public void invoke() throws SchedulerException
    {
        scheduleCalculateEvent();
        scheduleCalculateOnlineMetrics();
        scheduler.start();
    }

    private void scheduleCalculateEvent() throws SchedulerException
    {
        JobDetail job = newJob(CalculateEventMetricsScheduledJob.class)
                .withIdentity("calculateEventMetricsScheduledJob", "MetricsCollectors")
                .requestRecovery(false)
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity("triggerFor_calculateEventMetricsScheduledJob", "MetricsCollectors")
                .withSchedule(cronSchedule("0 0/5 * * * ?").inTimeZone(TimeZone.getTimeZone("UTC"))
                                      .withMisfireHandlingInstructionFireAndProceed())
                .forJob(job.getKey())
                .build();
        scheduleJobWithTriggerIfNotPresent(job, trigger);
    }

    private void scheduleCalculateOnlineMetrics() throws SchedulerException
    {
        JobDetail job = newJob(CalculateOnlineMetricsScheduledJob.class)
                .withIdentity("calculateOnlineMetricsScheduledJob", "MetricsCollectors")
                .requestRecovery(false)
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity("triggerFor_calculateOnlineMetricsScheduledJob", "MetricsCollectors")
                .withSchedule(cronSchedule("0/15 * * * * ?").inTimeZone(TimeZone.getTimeZone("UTC"))
                                      .withMisfireHandlingInstructionFireAndProceed())
                .forJob(job.getKey())
                .build();
        scheduleJobWithTriggerIfNotPresent(job, trigger);
    }

    private void scheduleJobWithTriggerIfNotPresent(JobDetail job, CronTrigger trigger) throws SchedulerException
    {
        if (!scheduler.checkExists(job.getKey()) && !scheduler.checkExists(trigger.getKey()))
        {
            try
            {
                scheduler.scheduleJob(job, trigger);
            } catch (ObjectAlreadyExistsException existsExc)
            {
                System.out.println("Someone has already scheduled such job/trigger. " + job.getKey() + " : " + trigger.getKey());
            }
        }
    }
}
