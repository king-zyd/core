package com.zyd.core.platform.monitor.exception;

import com.zyd.core.platform.monitor.Monitor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Chi
 */

@XmlRootElement(name = "recent-exceptions")
@XmlAccessorType(XmlAccessType.FIELD)
@Monitor("recent-exceptions")
public class RecentExceptions {
    @XmlElement(name = "exception")
    private final Queue<ExceptionInfo> exceptions = new ConcurrentLinkedDeque<>();

    @XmlTransient
    int maxRecentExceptions = 100;

    public void record(Throwable throwable) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setExceptionClass(throwable.getClass().getName());
        exceptionInfo.setExceptionMessage(throwable.getMessage());
        exceptionInfo.setOccurred(new Date());

        exceptions.add(exceptionInfo);

        while (exceptions.size() > maxRecentExceptions) exceptions.poll();
    }

    public Queue<ExceptionInfo> getExceptions() {
        return exceptions;
    }
}
