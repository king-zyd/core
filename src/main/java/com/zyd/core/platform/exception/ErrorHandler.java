package com.zyd.core.platform.exception;

import com.zyd.core.log.ActionLog;
import com.zyd.core.log.ActionLoggerImpl;
import com.zyd.core.log.ActionResult;
import com.zyd.core.platform.monitor.exception.ExceptionMonitor;
import com.zyd.core.platform.monitor.exception.RecentExceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    private ExceptionMonitor exceptionMonitor;

    private RecentExceptions recentExceptions;

    private ActionLoggerImpl actionLogger;

    public void handle(Throwable e) {
        if (isIgnore(e))
            return;
        if (isWarning(e)) {
            logWarning(e);
            exceptionMonitor.warn(e);
        } else {
            logError(e);
            exceptionMonitor.error(e);
            recentExceptions.record(e);
        }
    }

    boolean isIgnore(Throwable e) {
        if (null == e)
            return true;
        return e.getClass().isAnnotationPresent(Ignore.class);
    }

    boolean isWarning(Throwable e) {
        return e.getClass().isAnnotationPresent(Warning.class) || e instanceof HttpRequestMethodNotSupportedException || e instanceof BindException;
    }

    private void logError(Throwable e) {
        ActionLog actionLog = actionLogger.currentActionLog();
        actionLog.setResult(ActionResult.ERROR);
        logger.error(e.getMessage(), e);
        actionLog.setException(e.getClass().getName());
        actionLog.setErrorMessage(e.getMessage());
    }

    private void logWarning(Throwable e) {
        ActionLog actionLog = actionLogger.currentActionLog();
        actionLog.setResult(ActionResult.WARNING);
        logger.info(e.getMessage(), e);
        actionLog.setException(e.getClass().getName());
        actionLog.setErrorMessage(e.getMessage());
    }

    @Inject
    public void setExceptionMonitor(ExceptionMonitor exceptionMonitor) {
        this.exceptionMonitor = exceptionMonitor;
    }

    @Inject
    public void setActionLogger(ActionLoggerImpl actionLogger) {
        this.actionLogger = actionLogger;
    }

    @Inject
    public void setRecentExceptions(RecentExceptions recentExceptions) {
        this.recentExceptions = recentExceptions;
    }
}
