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

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Date;

/**
 * @author ilya40umov
 */
public class LongRunningBatchTask implements Tasklet
{
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
        Date scheduledFireTime = (Date) chunkContext.getStepContext().getJobParameters().get("scheduledFireTime");
        System.out.println(">>>> LongRunningBatchTask.execute( " + scheduledFireTime.toString() + " )");
        int i = 0;
        while (i < 600) // we don't pay attention if we are interrupted
        {
            i++;
            try
            {
                Thread.sleep(1000L);
            } catch (InterruptedException e)
            {
                Thread.interrupted();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
