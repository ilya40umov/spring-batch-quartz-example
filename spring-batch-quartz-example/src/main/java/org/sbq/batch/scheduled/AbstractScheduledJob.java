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
package org.sbq.batch.scheduled;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author ilya40umov
 */
public abstract class AbstractScheduledJob extends QuartzJobBean
{
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    protected JobRegistry getJobRegistry()
    {
        return applicationContext.getBean(JobRegistry.class);
    }

    protected JobLauncher getJobLauncher()
    {
        return applicationContext.getBean(JobLauncher.class);
    }

    protected JobExplorer getJobExplorer()
    {
        return applicationContext.getBean(JobExplorer.class);
    }

    protected JobOperator getJobOperator()
    {
        return applicationContext.getBean(JobOperator.class);
    }

    protected JobRepository getJobRepository()
    {
        return applicationContext.getBean(JobRepository.class);
    }
}
