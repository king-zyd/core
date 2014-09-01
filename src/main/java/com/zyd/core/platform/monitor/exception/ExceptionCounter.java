package com.zyd.core.platform.monitor.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionCounter {
    @XmlElement(name = "exception-class")
    private String exceptionClass;
    @XmlElement(name = "count")
    private int count;

    public void increase() {
        count++;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
