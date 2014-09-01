package com.zyd.core.log;

/**
 * @author neo
 */
public class LogSettings {
    private static final LogSettings INSTANCE = new LogSettings();

    public static LogSettings get() {
        return INSTANCE;
    }

    private boolean enableTraceLog = true;

    private boolean alwaysWriteTraceLog;

    private LogMessageFilter logMessageFilter;

    public boolean isEnableTraceLog() {
        return enableTraceLog;
    }

    public void setEnableTraceLog(boolean enableTraceLog) {
        this.enableTraceLog = enableTraceLog;
    }

    public boolean isAlwaysWriteTraceLog() {
        return alwaysWriteTraceLog;
    }

    public void setAlwaysWriteTraceLog(boolean alwaysWriteTraceLog) {
        this.alwaysWriteTraceLog = alwaysWriteTraceLog;
    }

    public LogMessageFilter getLogMessageFilter() {
        return logMessageFilter;
    }

    public void setLogMessageFilter(LogMessageFilter logMessageFilter) {
        this.logMessageFilter = logMessageFilter;
    }
}
