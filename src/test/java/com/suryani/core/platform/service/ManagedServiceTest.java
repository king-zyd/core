package com.zyd.core.platform.service;

import com.zyd.core.SpringServiceTest;
import com.zyd.core.platform.scheduler.Job;
import com.suryani.rest.MockScheduler;
import com.suryani.rest.platform.service.TestManagedService;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ManagedServiceTest extends SpringServiceTest {
    @Inject
    MockScheduler scheduler;

    @Test
    public void serviceShouldBeInitialized() {
        ManagedServiceInitializeJob job = findInitializeJob();
        Assert.assertNotNull(job);
    }

    private ManagedServiceInitializeJob findInitializeJob() {
        for (Job job : scheduler.getRegisteredJobs()) {
            if (job instanceof ManagedServiceInitializeJob && TestManagedService.class.equals(((ManagedServiceInitializeJob) job).service.getClass())) {
                return (ManagedServiceInitializeJob) job;
            }
        }
        return null;
    }
}
