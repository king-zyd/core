package com.zyd.core.platform.scheduler;

import com.zyd.core.log.LogConstants;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.scheduler.factory.JobClassFactory;
import com.zyd.core.platform.scheduler.factory.JobFactory;
import com.zyd.core.platform.scheduler.factory.JobInstanceFactory;
import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.DateUtils;
import com.zyd.core.util.TimeLength;
import org.slf4j.MDC;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author neo
 */
public class SchedulerImpl implements Scheduler {
    private static final int CORE_POOL_SIZE = 10;
    private final ScheduledThreadPoolExecutor executor;
    private final ConcurrentTaskScheduler scheduler;
    private final Map<String, JobFactory> jobFactories = new HashMap<>();
    private SpringObjectFactory springObjectFactory;

    public SchedulerImpl() {
        executor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
        scheduler = new ConcurrentTaskScheduler(executor);
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdownNow();
    }

    @Override
    public void triggerOnce(Job job) {
        triggerOnceAt(job, new Date());
    }

    @Override
    public void triggerOnceWithDelay(Job job, TimeLength delay) {
        Date time = DateUtils.add(new Date(), Calendar.SECOND, (int) delay.toSeconds());
        triggerOnceAt(job, time);
    }

    @Override
    public void triggerOnceAt(Job job, Date time) {
        JobExecutor jobExecutor = springObjectFactory.createBean(JobExecutor.class);
        jobExecutor.setJobFactory(new JobInstanceFactory(springObjectFactory, job));
        String requestId = MDC.get(LogConstants.MDC_REQUEST_ID);
        jobExecutor.setRequestId(requestId);
        scheduler.schedule(jobExecutor, time);
    }

    public void triggerJobOnce(String jobId) {
        JobExecutor jobExecutor = springObjectFactory.createBean(JobExecutor.class);
        JobFactory jobFactory = jobFactories.get(jobId);
        if (jobFactory == null) throw new ResourceNotFoundException("can not find job, jobId=" + jobId);
        jobExecutor.setJobFactory(jobFactory);
        String requestId = MDC.get(LogConstants.MDC_REQUEST_ID);
        jobExecutor.setRequestId(requestId);
        scheduler.schedule(jobExecutor, new Date());
    }

    public JobExecutor classJob(String jobId, Class<? extends Job> jobClass) {
        AssertUtils.assertHasText(jobId, "jobId cannot be null");
        AssertUtils.assertNotNull(jobClass, "jobClass cannot be null");
        JobClassFactory jobFactory = new JobClassFactory(springObjectFactory, jobClass, jobId);
        return createJobExecutor(jobId, jobFactory);
    }

    public JobExecutor instanceJob(String jobId, Job jobInstance) {
        AssertUtils.assertHasText(jobId, "jobId cannot be null");
        AssertUtils.assertNotNull(jobInstance, "jobInstance cannot be null");
        jobInstance.jobId.set(jobId);
        JobInstanceFactory jobFactory = new JobInstanceFactory(springObjectFactory, jobInstance);
        return createJobExecutor(jobInstance.jobId.value(), jobFactory);
    }

    private JobExecutor createJobExecutor(String jobId, JobFactory jobFactory) {
        JobExecutor jobExecutor = springObjectFactory.createBean(JobExecutor.class);
        jobExecutor.setJobFactory(jobFactory);
        JobFactory previous = jobFactories.put(jobId, jobFactory);
        AssertUtils.assertNull(previous, "found duplicated jobId, jobId={}, previous={}, current={}", jobId, previous, jobFactory);
        return jobExecutor;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }
}
