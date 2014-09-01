package com.zyd.core.platform.service;

import com.zyd.core.platform.scheduler.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neo
 */
public class ManagedServiceInitializeJob extends Job {
    private final Logger logger = LoggerFactory.getLogger(ManagedServiceInitializeJob.class);

    final ManagedService service;

    public ManagedServiceInitializeJob(ManagedService service) {
        this.service = service;
    }

    @Override
    public void execute() throws Throwable {
        logger.info("start initializing service, serviceClass={}", service.getClass().getName());
        service.doInitialize();
        logger.info("finish initializing service, serviceClass={}", service.getClass().getName());
    }
}
