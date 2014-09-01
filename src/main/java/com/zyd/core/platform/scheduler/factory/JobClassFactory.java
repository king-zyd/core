package com.zyd.core.platform.scheduler.factory;

import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.scheduler.Job;

/**
 * @author neo
 */
public class JobClassFactory implements JobFactory {
    private final SpringObjectFactory objectFactory;
    private final Class<? extends Job> jobClass;
    private final String jobId;

    public JobClassFactory(SpringObjectFactory objectFactory, Class<? extends Job> jobClass, String jobId) {
        this.objectFactory = objectFactory;
        this.jobClass = jobClass;
        this.jobId = jobId;
    }

    @Override
    public Job create() {
        Job job = objectFactory.createBean(jobClass);
        job.jobId.set(jobId);
        return job;
    }

    @Override
    public String toString() {
        return String.format("JobClassFactory{jobId=%s, jobClass=%s}", jobId, jobClass);
    }
}
