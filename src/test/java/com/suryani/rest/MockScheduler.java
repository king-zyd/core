package com.suryani.rest;

import com.zyd.core.platform.scheduler.Job;
import com.zyd.core.platform.scheduler.SchedulerImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author neo
 */
public class MockScheduler extends SchedulerImpl {
    private final List<Job> registeredJobs = new ArrayList<>();

    @Override
    public void triggerOnceAt(Job job, Date time) {
        registeredJobs.add(job);
    }

    @Override
    public TaskScheduler getScheduler() {
        return new MockTaskScheduler();
    }

    public List<Job> getRegisteredJobs() {
        return registeredJobs;
    }

    private static class MockTaskScheduler implements TaskScheduler {
        @Override
        public ScheduledFuture schedule(Runnable task, Trigger trigger) {
            return null;
        }

        @Override
        public ScheduledFuture schedule(Runnable task, Date startTime) {
            return null;
        }

        @Override
        public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period) {
            return null;
        }

        @Override
        public ScheduledFuture scheduleAtFixedRate(Runnable task, long period) {
            return null;
        }

        @Override
        public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
            return null;
        }

        @Override
        public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay) {
            return null;
        }
    }
}
