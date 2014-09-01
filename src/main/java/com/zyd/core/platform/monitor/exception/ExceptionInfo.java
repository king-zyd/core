package com.zyd.core.platform.monitor.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * @author neo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionInfo {
    @XmlElement(name = "exception-class")
    private String exceptionClass;
    @XmlElement(name = "message")
    private String exceptionMessage;
    @XmlElement(name = "time")
    private Date occurred;

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Date getOccurred() {
        return occurred;
    }

    public void setOccurred(Date occurred) {
        this.occurred = occurred;
    }
}
