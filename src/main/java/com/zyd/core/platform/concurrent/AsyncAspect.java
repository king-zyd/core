package com.zyd.core.platform.concurrent;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Chi
 */
@Aspect
public class AsyncAspect {
    private final Logger logger = LoggerFactory.getLogger(AsyncAspect.class);

    private TaskExecutor taskExecutor;

    @Around("@annotation(async)")
    public Object execute(final ProceedingJoinPoint joinPoint, Async async) throws Throwable {
        final long timeout = async.timeout();

        logger.debug("start async call, method={}, timeout={}", joinPoint.getSignature(), timeout);

        Future<Object> future = taskExecutor.execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    return joinPoint.proceed();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable t) {
                    throw new TaskExecutionException(t);
                }
            }
        });

        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new TaskTimeoutException(e);
        }
    }

    @Inject
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
