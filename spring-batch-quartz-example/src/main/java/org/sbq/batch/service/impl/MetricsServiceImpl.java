package org.sbq.batch.service.impl;

import org.sbq.batch.exceptions.TransientException;
import org.sbq.batch.service.MetricsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * @author ilya40umov
 */
@Service
public class MetricsServiceImpl implements MetricsService
{
    @Override
    public void calculateEventMetrics(Date startingFrom, Date endingAt)
    {
        System.out.println(">>>> calculateEventMetrics( " + startingFrom.toString() + " , " + endingAt.toString() + " )");

        // TODO implement the actual logic(see README.md for details)
    }

    @Override
    public void calculateOnlineMetrics(Date at)
    {
        System.out.println(">>>> calculateOnlineMetrics( " + at.toString() + " )");
        Random rnd = new Random();
        if (rnd.nextInt(3) == 0)
        {
            System.out.println(">>>> calculateOnlineMetrics() - TRANSIENT EXCEPTION...");
            throw new TransientException("This kind of problem can sometimes happen!");
        }

        // TODO implement the actual logic(see README.md for details)
    }
}
