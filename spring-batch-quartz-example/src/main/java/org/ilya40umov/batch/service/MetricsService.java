package org.ilya40umov.batch.service;

import java.util.Date;

/**
 * @author ilya40umov
 */
public interface MetricsService
{
    void calculateEventMetrics(Date startingFrom, Date endingAt);

    void calculateOnlineMetrics(Date at);

}
