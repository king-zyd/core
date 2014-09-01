package com.zyd.core.platform;

import com.zyd.core.platform.scheduler.Job;
import com.zyd.core.platform.scheduler.SchedulerImpl;
import com.zyd.core.platform.scheduler.info.JobStatistic;
import com.zyd.core.util.TimeLength;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.UUID;

/**
 * @author neo
 */
public class JobRegistry {
    private final ScheduledTaskRegistrar schedulerRegistry;
    private final JobStatistic jobStatistic;
    private final SchedulerImpl scheduler;

    public JobRegistry(ScheduledTaskRegistrar schedulerRegistry, JobStatistic jobStatistic, SchedulerImpl scheduler) {
        this.schedulerRegistry = schedulerRegistry;
        this.jobStatistic = jobStatistic;
        this.scheduler = scheduler;
    }

    // specify a meaningful jobId
    @Deprecated
    public void triggerWithFixedDelay(Class<? extends Job> jobClass, TimeLength delay) {
        String jobId = UUID.randomUUID().toString();
        triggerWithFixedDelay(jobId, jobClass, delay);
    }

    // specify a meaningful jobId
    @Deprecated
    public void triggerAtFixedRate(Class<? extends Job> jobClass, TimeLength interval) {
        String jobId = UUID.randomUUID().toString();
        triggerAtFixedRate(jobId, jobClass, interval);
    }

    // specify a meaningful jobId
    @Deprecated
    public void triggerByCronExpression(Class<? extends Job> jobClass, String cronExpression) {
        String jobId = UUID.randomUUID().toString();
        triggerByCronExpression(jobId, jobClass, cronExpression);
    }

    public void triggerWithFixedDelay(String jobId, Class<? extends Job> jobClass, TimeLength delay) {
        schedulerRegistry.addFixedDelayTask(scheduler.classJob(jobId, jobClass), delay.toMilliseconds());
        jobStatistic.registerJob(jobId, jobClass, "fixed_delay: " + delay.toSeconds() + " seconds");
    }

    public void triggerAtFixedRate(String jobId, Class<? extends Job> jobClass, TimeLength interval) {
        schedulerRegistry.addFixedRateTask(scheduler.classJob(jobId, jobClass), interval.toMilliseconds());
        jobStatistic.registerJob(jobId, jobClass, "fixed_rate: " + interval.toSeconds() + " seconds");
    }

    public void triggerByCronExpression(String jobId, Class<? extends Job> jobClass, String cronExpression) {
        jobStatistic.registerJob(jobId, jobClass, "cron: " + cronExpression);
        schedulerRegistry.addCronTask(scheduler.classJob(jobId, jobClass), cronExpression);
    }

    public void triggerWithFixedDelay(String jobId, Job jobInstance, TimeLength delay) {
        schedulerRegistry.addFixedDelayTask(scheduler.instanceJob(jobId, jobInstance), delay.toMilliseconds());
        jobStatistic.registerJob(jobInstance.jobId.value(), jobInstance.getClass(), "fixed_delay: " + delay.toSeconds() + " seconds");
    }

    public void triggerAtFixedRate(String jobId, Job jobInstance, TimeLength interval) {
        schedulerRegistry.addFixedRateTask(scheduler.instanceJob(jobId, jobInstance), interval.toMilliseconds());
        jobStatistic.registerJob(jobInstance.jobId.value(), jobInstance.getClass(), "fixed_rate: " + interval.toSeconds() + " seconds");
    }

    public void triggerByCronExpression(String jobId, Job jobInstance, String cronExpression) {
        schedulerRegistry.addCronTask(scheduler.instanceJob(jobId, jobInstance), cronExpression);
        jobStatistic.registerJob(jobInstance.jobId.value(), jobInstance.getClass(), "cron: " + cronExpression);
    }
}
