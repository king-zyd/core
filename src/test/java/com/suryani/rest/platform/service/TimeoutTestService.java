package com.suryani.rest.platform.service;

import com.zyd.core.platform.concurrent.Async;
import org.springframework.stereotype.Service;

/**
 * @author Chi
 */
@Service
public class TimeoutTestService {
    @Async(timeout = 2000)
    public int notTimeout() {
        return 1;
    }

    @Async(timeout = 200)
    public int timeout() throws InterruptedException {
        Thread.sleep(2000);
        return 1;
    }
}
