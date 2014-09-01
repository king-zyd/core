package com.zyd.core.platform.monitor.exception;

import com.zyd.core.json.JSONBinder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class ExceptionMonitorTest {
    ExceptionMonitor exceptionMonitor;

    @Before
    public void createExceptionMonitor() {
        exceptionMonitor = new ExceptionMonitor();
    }

    @Test
    public void countError() {
        exceptionMonitor.error(new RuntimeException());

        assertEquals(1, exceptionMonitor.getErrors());
        assertEquals(1, exceptionMonitor.getExceptionCounters().size());
        assertEquals(1, exceptionMonitor.getExceptionCounters().get(0).getCount());
    }

    @Test
    public void countWarning() {
        exceptionMonitor.warn(new RuntimeException());

        assertEquals(1, exceptionMonitor.getWarnings());
    }

    @Test
    public void serializeToJSON() {
        exceptionMonitor.error(new RuntimeException());
        exceptionMonitor.warn(new RuntimeException());

        String json = JSONBinder.binder(ExceptionMonitor.class).toJSON(exceptionMonitor);
        assertThat(json, containsString("\"errors\":1"));
    }
}
