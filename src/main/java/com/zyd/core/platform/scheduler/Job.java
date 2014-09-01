package com.zyd.core.platform.scheduler;

import com.zyd.core.util.ReadOnly;

/**
 * @author neo
 */
public abstract class Job {
    public final ReadOnly<String> jobId = new ReadOnly<>();

    protected abstract void execute() throws Throwable;
}
