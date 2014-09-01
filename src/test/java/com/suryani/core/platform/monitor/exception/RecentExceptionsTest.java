package com.zyd.core.platform.monitor.exception;

import com.zyd.core.json.JSONBinder;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Chi
 */
public class RecentExceptionsTest {
    private RecentExceptions recentExceptions;

    @Before
    public void createRecentExceptions() {
        recentExceptions = new RecentExceptions();
    }

    @Test
    public void record() {
        recentExceptions.maxRecentExceptions = 1;

        for (int i = 0; i < 2; i++) {
            recentExceptions.record(new RuntimeException());
        }

        assertEquals(1, recentExceptions.getExceptions().size());
    }

    @Test
    public void serializeToJSON() {
        recentExceptions.record(new RuntimeException());
        String json = JSONBinder.binder(RecentExceptions.class).toJSON(recentExceptions);

        assertThat(json, JUnitMatchers.containsString("\"exception-class\""));
    }
}
