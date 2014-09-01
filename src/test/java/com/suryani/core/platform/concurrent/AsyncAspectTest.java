package com.zyd.core.platform.concurrent;

import com.zyd.core.SpringServiceTest;
import com.suryani.rest.platform.service.TimeoutTestService;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.inject.Inject;

/**
 * @author Chi
 */
public class AsyncAspectTest extends SpringServiceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Inject
    private TimeoutTestService timeoutTestService;

    @Test
    public void executeWithoutTimeout() {
        int result = timeoutTestService.notTimeout();

        Assert.assertEquals(1, result);
    }

    @Test
    public void timeoutOnExecution() throws InterruptedException {
        exception.expect(TaskTimeoutException.class);

        timeoutTestService.timeout();
    }
}
