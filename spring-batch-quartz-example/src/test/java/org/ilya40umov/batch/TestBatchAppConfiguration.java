package org.ilya40umov.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

/**
 * @author ilya40umov
 */
@Test
public class TestBatchAppConfiguration
{
    /**
     * This test simply checks whether ApplicationContext can be initialized without errors.
     */
    public void testSpringContextStart()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchAppConfiguration.class);
    }

}
