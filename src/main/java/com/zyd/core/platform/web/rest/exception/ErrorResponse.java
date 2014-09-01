package com.zyd.core.platform.web.rest.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse {
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "exception-trace")
    private String exceptionTrace;
    @XmlElement(name = "request-id")
    private String requestId;
    @XmlElement(name = "exception-class")
    private String exceptionClass;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionTrace() {
        return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }
}
