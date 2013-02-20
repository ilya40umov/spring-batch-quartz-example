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
package org.ilya40umov.batch.tasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author ilya40umov
 */
public class CalculateEventMetricsTask extends AbstractScheduledTask
{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        System.out.println(">>>> CalculateEventMetricsTask.execute()");
    }
    // this is a quartz job which should run every 5 minutes calculating the following metrics:
    // number of occurrences for each type of event for 5 last minutes

    // miss-fire handling(being offline): should catch up using historical data

    // XXX should execute CalculateEventMetricsJob
}
