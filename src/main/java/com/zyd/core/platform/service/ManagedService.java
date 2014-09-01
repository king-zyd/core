package com.zyd.core.platform.service;

import com.zyd.core.platform.exception.InternalServiceException;
import com.zyd.core.platform.monitor.ServiceMonitor;
import com.zyd.core.platform.monitor.ServiceStatus;
import com.zyd.core.util.TimeLength;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author neo
 */
public abstract class ManagedService implements ServiceMonitor {
    private final CountDownLatch startSignal = new CountDownLatch(1);
    private ManagedServiceStatus status = ManagedServiceStatus.NOT_INITIALIZED;

    public abstract void initialize() throws Throwable;

    final void doInitialize() throws Throwable {
        try {
            initialize();
            status = ManagedServiceStatus.INITIALIZED;
            startSignal.countDown();
        } catch (Throwable e) {
            status = ManagedServiceStatus.FAILED_TO_INITIALIZED;
            throw e;
        }
    }

    protected final void waitForInitialization(TimeLength awaitTime) {
        if (ManagedServiceStatus.INITIALIZED.equals(status)) return;
        if (ManagedServiceStatus.FAILED_TO_INITIALIZED.equals(status)) {
            throw new InternalServiceException("service failed to initializing, please check service init error log");
        }
        try {
            startSignal.await(awaitTime.toSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new InternalServiceException("wait service initialization timed out", e);
        }
    }

    protected final boolean isInitialized() {
        return ManagedServiceStatus.INITIALIZED.equals(status);
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        return isInitialized() ? ServiceStatus.UP : ServiceStatus.DOWN;
    }

    @Override
    public String getServiceName() {
        return getClass().getSimpleName();
    }
}
