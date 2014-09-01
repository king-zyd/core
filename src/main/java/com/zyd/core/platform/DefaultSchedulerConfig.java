package com.zyd.core.platform;

import com.zyd.core.platform.scheduler.SchedulerImpl;
import com.zyd.core.platform.scheduler.info.JobStatistic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.inject.Inject;

/**
 * @author neo
 */
@EnableScheduling
public abstract class DefaultSchedulerConfig implements SchedulingConfigurer {
    @Inject
    SchedulerImpl scheduler;

    @Inject
    JobStatistic jobStatistic;

    @Inject
    SpringObjectFactory springObjectFactory;

    @Override
    public void configureTasks(ScheduledTaskRegistrar registry) {
        registry.setTaskScheduler(scheduler.getScheduler());
        try {
            configure(new JobRegistry(registry, jobStatistic, scheduler));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("failed to configure scheduler jobs, errorMessage=" + e.getMessage(), e);
        }
    }

    protected abstract void configure(JobRegistry registry) throws Exception;
}
