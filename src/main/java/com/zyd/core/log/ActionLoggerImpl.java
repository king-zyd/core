package com.zyd.core.log;

import com.zyd.core.util.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author neo
 */
public class ActionLoggerImpl implements ActionLogger {
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String LOG_SPLITTER = " | ";

    // the reason not using Spring RequestScope is because we need to handle web/scheduler/task executor
    private static final ActionLoggerImpl INSTANCE = new ActionLoggerImpl();

    public static ActionLoggerImpl get() {
        return INSTANCE;
    }

    private final Logger logger = LoggerFactory.getLogger(ActionLogger.class);
    private final ConcurrentMap<Long, ActionLog> logs = new ConcurrentHashMap<>();

    public void initialize() {
        long threadId = Thread.currentThread().getId();
        logs.put(threadId, new ActionLog());
    }

    @Override
    public void logContext(String key, String value) {
        currentActionLog().logContext(key, value);
    }

    private long getTargetThreadId() {
        String targetThreadId = MDC.get(LogConstants.MDC_TARGET_THREAD_ID);
        return Convert.toLong(targetThreadId, Thread.currentThread().getId());
    }

    public ActionLog currentActionLog() {
        return logs.get(getTargetThreadId());
    }

    public void setCurrentAction(String action) {
        currentActionLog().setAction(action);
    }

    public void save() {
        long threadId = Thread.currentThread().getId();
        ActionLog actionLog = logs.remove(threadId);
        actionLog.setElapsedTime(System.currentTimeMillis() - actionLog.getRequestDate().getTime());
        logger.info(buildActionLogText(actionLog));
    }

    private String buildActionLogText(ActionLog actionLog) {
        StringBuilder builder = new StringBuilder();
        builder.append(Convert.toString(actionLog.getRequestDate(), LOG_DATE_FORMAT))
                .append(LOG_SPLITTER)
                .append(actionLog.getResult())
                .append(LOG_SPLITTER)
                .append(actionLog.getRequestId())
                .append(LOG_SPLITTER)
                .append(actionLog.getAction())
                .append(LOG_SPLITTER)
                .append(actionLog.getElapsedTime())
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getClientIP()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getRequestURI()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPMethod()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPStatusCode()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getException()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getErrorMessage()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getTraceLogPath()));

        Map<String, String> context = actionLog.getContext();
        if (context != null) {
            for (Map.Entry<String, String> entry : context.entrySet()) {
                builder.append(LOG_SPLITTER)
                        .append(entry.getKey()).append('=').append(entry.getValue());
            }
        }

        return builder.toString();
    }

    private String buildLogField(Object field) {
        return field == null ? "" : String.valueOf(field);
    }
}
