package com.zyd.core.platform.scheduler;

import com.zyd.core.SpringServiceTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class SchedulerTest extends SpringServiceTest {
    @Inject
    Scheduler scheduler;

    @Test
    public void schedulerShouldBeRegistered() {
        Assert.assertNotNull("Scheduler should be registered by DefaultSchedulerConfig", scheduler);
    }
}
