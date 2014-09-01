package com.zyd.core.platform.monitor;

import com.zyd.core.SpringSiteTest;
import junit.framework.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Chi
 */
public class MonitorManagerTest extends SpringSiteTest {
    @Inject
    MonitorManager monitorManager;

    @Test
    public void getExceptionsMonitorBean() throws Exception {
        Object bean = monitorManager.monitorBean("exceptions");

        Assert.assertNotNull(bean);
    }
}
