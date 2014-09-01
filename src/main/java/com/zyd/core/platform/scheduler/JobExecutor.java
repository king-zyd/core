package com.zyd.core.platform.scheduler;

import com.zyd.core.log.ActionLoggerImpl;
import com.zyd.core.log.ActionResult;
import com.zyd.core.log.LogConstants;
import com.zyd.core.log.TraceLogger;
import com.zyd.core.platform.ClassUtils;
import com.zyd.core.platform.exception.ErrorHandler;
import com.zyd.core.platform.scheduler.factory.JobFactory;
import com.zyd.core.platform.scheduler.info.JobStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author neo
 */
public class JobExecutor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    private ErrorHandler errorHandler;
    private String requestId;
    private JobFactory jobFactory;
    private JobStatistic jobStatistic;
    private ActionLoggerImpl actionLogger;

    @Override
    public void run() {
        TraceLogger.get().initialize();
        actionLogger.initialize();
        Job job = null;
        try {
            job = jobFactory.create();
            logger.debug("start executing job, jobClass={}", job.getClass().getName());

            assignAction(job);
            assignRequestId();

            if (job.jobId.assigned())
                logger.debug("jobId={}", job.jobId.value());

            recordStart(job);
            job.execute();
            recordSucceed(job);
        } catch (Throwable e) {
            errorHandler.handle(e);
            recordFailed(job);
        } finally {
            logger.debug("finish executing job");
            actionLogger.save();
            TraceLogger.get().cleanup(false);
        }
    }

    private void assignAction(Job job) {
        StringBuilder action = new StringBuilder();
        action.append(ClassUtils.getSimpleOriginalClassName(job));
        if (job.jobId.assigned()) {
            action.append('-').append(job.jobId.value());
        }
        MDC.put(LogConstants.MDC_ACTION, action.toString());
        actionLogger.currentActionLog().setAction(action.toString());
        logger.debug("action={}", action);
    }

    private void assignRequestId() {
        if (requestId == null) {
            logger.debug("request headers do not contain request-id, generate new one");
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(LogConstants.MDC_REQUEST_ID, requestId);
        actionLogger.currentActionLog().setRequestId(requestId);
        logger.debug("requestId={}", requestId);
    }

    private void recordStart(Job job) {
        if (job.jobId.assigned())
            jobStatistic.start(job.jobId.value());
    }

    private void recordFailed(Job job) {
        if (job != null && job.jobId.assigned())
            jobStatistic.failed(job.jobId.value());
    }

    private void recordSucceed(Job job) {
        if (job.jobId.assigned())
            jobStatistic.succeed(job.jobId.value());

        actionLogger.currentActionLog().setResult(ActionResult.SUCCESS);
    }

    public void setJobFactory(JobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Inject
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Inject
    public void setJobStatistic(JobStatistic jobStatistic) {
        this.jobStatistic = jobStatistic;
    }

    @Inject
    public void setActionLogger(ActionLoggerImpl actionLogger) {
        this.actionLogger = actionLogger;
    }
}
