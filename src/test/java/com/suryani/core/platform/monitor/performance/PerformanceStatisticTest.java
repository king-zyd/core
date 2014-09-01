package com.zyd.core.platform.monitor.performance;

import com.zyd.core.util.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * @author neo
 */
public class PerformanceStatisticTest {
    PerformanceStatistic statistic;

    @Before
    public void createPerformanceStatistic() {
        statistic = new PerformanceStatistic() {
            @Override
            int getMaxCountOfSlowExecutions() {
                return 3;
            }
        };
    }

    @Test
    public void keepTopSlowestExecutions() {
        statistic.record(1000, DateUtils.date(2012, Calendar.JANUARY, 1));
        statistic.record(2000, DateUtils.date(2012, Calendar.JANUARY, 2));
        statistic.record(1500, DateUtils.date(2012, Calendar.JANUARY, 3));
        statistic.record(500, DateUtils.date(2012, Calendar.JANUARY, 4));

        Assert.assertEquals(3, statistic.topSlowestExecutions.size());
        Assert.assertEquals(2000, statistic.topSlowestExecutions.first().getElapsedTime(), 0);
        Assert.assertEquals(1000, statistic.topSlowestExecutions.last().getElapsedTime(), 0);
    }

    @Test
    public void getAverageElapsedTime() {
        statistic.totalElapsedTime = 3200;
        statistic.totalExecutions = 3;

        Assert.assertEquals(1067, statistic.getAverageElapsedTime(), 0);
    }
}
