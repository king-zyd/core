package com.zyd.core.platform.monitor.performance;

import com.zyd.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class PerformanceStatisticsTest {
    PerformanceStatistics statistics;

    @Before
    public void createPerformanceStatistics() {
        statistics = new PerformanceStatistics();
    }

    @Test
    public void toXML() {
        statistics.record("action1", 1000, DateUtils.date(2012, Calendar.JANUARY, 1));
        statistics.record("action1", 2000, DateUtils.date(2012, Calendar.JANUARY, 2));
        statistics.record("action2", 1500, DateUtils.date(2012, Calendar.JANUARY, 3));

        String xml = statistics.toXML();

        assertThat(xml, containsString("<action name=\"action1\">"));
        assertThat(xml, containsString("<action name=\"action2\">"));
    }
}
