package com.zyd.core.platform.service;

import com.zyd.core.log.LogConstants;
import com.zyd.core.platform.scheduler.Scheduler;
import com.zyd.core.platform.scheduler.info.JobStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author neo
 */
public class ManagedServiceApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ManagedServiceApplicationListener.class);
    private static final String JOB_ID = "init-managed-service-job";

    private Scheduler scheduler;
    private JobStatistic jobStatistic;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, ManagedService> beans = context.getBeansOfType(ManagedService.class);
        for (Map.Entry<String, ManagedService> entry : beans.entrySet()) {
            ManagedService service = entry.getValue();
            logger.info("initializing service, name={}, serviceClass={}", entry.getKey(), service.getClass().getName());
            jobStatistic.registerJob(JOB_ID, ManagedServiceInitializeJob.class, "service_init: " + service.getClass().getName());
            MDC.put(LogConstants.MDC_REQUEST_ID, UUID.randomUUID().toString());
            ManagedServiceInitializeJob job = new ManagedServiceInitializeJob(service);
            job.jobId.set(JOB_ID);
            scheduler.triggerOnceAt(job, new Date());
        }
    }

    @Inject
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Inject
    public void setJobStatistic(JobStatistic jobStatistic) {
        this.jobStatistic = jobStatistic;
    }
}
