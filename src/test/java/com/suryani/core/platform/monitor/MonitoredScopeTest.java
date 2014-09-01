package com.zyd.core.platform.monitor;

import com.zyd.core.SpringSiteTest;
import com.zyd.core.platform.monitor.exception.ExceptionMonitor;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertSame;

/**
 * @author Chi
 */
public class MonitoredScopeTest extends SpringSiteTest {
    @Inject
    ExceptionMonitor instance1;

    @Inject
    ExceptionMonitor instance2;

    @Test
    public void monitorBeanIsSingleton() {
        assertSame(instance1, instance2);
    }
}
