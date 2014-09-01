package com.zyd.core.platform.scheduler.factory;

import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.scheduler.Job;

/**
 * @author neo
 */
public class JobInstanceFactory implements JobFactory {
    private final SpringObjectFactory objectFactory;
    private final Job job;

    public JobInstanceFactory(SpringObjectFactory objectFactory, Job job) {
        this.objectFactory = objectFactory;
        this.job = job;
    }

    @Override
    public Job create() {
        return objectFactory.initializeBean(job);
    }

    @Override
    public String toString() {
        return String.format("JobInstanceFactory{jobId=%s, jobClass=%s}", job.jobId, job.getClass());
    }
}
