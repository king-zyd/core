package com.zyd.core.platform.monitor.performance;

import java.util.Date;

/**
 * @author neo
 */
public class ExecutionInfo {
    private Date executionDate;
    private double elapsedTime;

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
