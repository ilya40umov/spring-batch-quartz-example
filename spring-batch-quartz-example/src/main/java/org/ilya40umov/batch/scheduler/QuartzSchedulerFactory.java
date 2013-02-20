package org.ilya40umov.batch.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author ilya40umov
 */
@Component
public class QuartzSchedulerFactory extends SchedulerFactoryBean
{
    @Autowired(required = true)
    private DataSource dataSource;

    @Autowired(required = true)
    private PlatformTransactionManager platformTransactionManager;

    @Autowired(required = true)
    private ResourceLoader resourceLoader;

    /**
     * XXX This method is invoked by Spring before afterPropertiesSet().
     */
    @PostConstruct
    private void setUp()
    {
        setDataSource(dataSource);
        setTransactionManager(platformTransactionManager);
        setConfigLocation(resourceLoader.getResource("classpath:/quartz.properties"));
        setApplicationContextSchedulerContextKey("applicationContext");
        setAutoStartup(false);
        setWaitForJobsToCompleteOnShutdown(true);
        setSchedulerName("SpringBatchScheduler");
    }

}
