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
package org.sbq.batch.tasks;

import org.sbq.batch.service.MetricsService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author ilya40umov
 */
public class CalculateEventMetricsTask implements Tasklet
{
    @Autowired
    private MetricsService metricsService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
    {
        Date startingFrom = (Date) chunkContext.getStepContext().getJobParameters().get("startingFrom");
        Date endingAt = (Date) chunkContext.getStepContext().getJobParameters().get("endingAt");
        metricsService.calculateEventMetrics(startingFrom, endingAt);
        return RepeatStatus.FINISHED;
    }

}
