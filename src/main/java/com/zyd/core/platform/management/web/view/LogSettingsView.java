package com.zyd.core.platform.management.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "log-settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogSettingsView {
    @XmlElement(name = "enable-trace-log")
    private boolean enableTraceLog;

    @XmlElement(name = "always-write-trace-log")
    private boolean alwaysWriteTraceLog;

    @XmlElement(name = "message-filter-class")
    private String messageFilterClass;

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

    public String getMessageFilterClass() {
        return messageFilterClass;
    }

    public void setMessageFilterClass(String messageFilterClass) {
        this.messageFilterClass = messageFilterClass;
    }
}
