package com.zyd.core.log;

import ch.qos.logback.classic.PatternLayout;
import com.zyd.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class LoggingEventProcessorTest {
    LoggingEventProcessor processor;

    @Before
    public void createLoggingEventProcessor() {
        processor = new LoggingEventProcessor(new PatternLayout(), "/log");
    }

    @Test
    public void generateLogFilePath() {
        String logFilePath = processor.generateLogFilePath("someController-method", DateUtils.date(2012, Calendar.OCTOBER, 2, 14, 5, 0), "requestId");
        assertThat(logFilePath, containsString("/log/2012/10/02/someController-method/1405.requestId."));
    }
}
