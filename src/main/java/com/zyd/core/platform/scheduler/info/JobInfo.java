package com.zyd.core.platform.scheduler.info;

import com.zyd.core.util.Convert;

import java.util.Date;

/**
 * @author neo
 */
public class JobInfo {
    private String jobId;
    private String jobClass;
    private String description;
    private Date lastStarted;
    private Date lastFinished;
    private String lastResult;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getLastStartedText() {
        if (lastStarted == null) return null;
        return Convert.toString(lastStarted, Convert.DATE_FORMAT_DATETIME);
    }

    public String getLastFinishedText() {
        if (lastFinished == null) return null;
        return Convert.toString(lastFinished, Convert.DATE_FORMAT_DATETIME);
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastStarted(Date lastStarted) {
        this.lastStarted = lastStarted;
    }

    public void setLastFinished(Date lastFinished) {
        this.lastFinished = lastFinished;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }
}
