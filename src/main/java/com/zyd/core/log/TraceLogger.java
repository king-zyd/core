package com.zyd.core.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.zyd.core.util.Convert;
import com.zyd.core.util.ExceptionUtils;
import com.zyd.core.util.RuntimeIOException;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author neo
 */
public class TraceLogger {
    private static final TraceLogger INSTANCE = new TraceLogger();

    public static TraceLogger get() {
        return INSTANCE;
    }

    private final ConcurrentMap<Long, LoggingEventProcessor> processors = new ConcurrentHashMap<>();
    private PatternLayout layout;
    private String logFolder;

    public void process(ILoggingEvent event) throws IOException {
        long threadId = getTargetThreadId();
        LoggingEventProcessor processor = processors.get(threadId);

        // ignore if current thread log is not explicitly initialized, for processor == null
        if (LogSettings.get().isEnableTraceLog() && processor != null) {
            processor.process(event);
        }
    }

    private long getTargetThreadId() {
        String targetThreadId = MDC.get(LogConstants.MDC_TARGET_THREAD_ID);
        return Convert.toLong(targetThreadId, Thread.currentThread().getId());
    }

    public void initialize() {
        MDC.clear();
        long threadId = Thread.currentThread().getId();
        processors.put(threadId, new LoggingEventProcessor(layout, logFolder));
    }

    public void cleanup(boolean forceFlushTraceLog) {
        try {
            long threadId = Thread.currentThread().getId();
            LoggingEventProcessor processor = processors.remove(threadId);
            processor.cleanup(forceFlushTraceLog || LogSettings.get().isAlwaysWriteTraceLog());
        } catch (IOException e) {
            System.err.println("failed to clean up TraceLogger, exception=" + ExceptionUtils.stackTrace(e));
            throw new RuntimeIOException(e);
        }
    }

    public void clearAll() {
        processors.clear();
    }

    public void setLayout(PatternLayout layout) {
        this.layout = layout;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }
}
