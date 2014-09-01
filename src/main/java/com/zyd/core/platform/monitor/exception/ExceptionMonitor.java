package com.zyd.core.platform.monitor.exception;

import com.zyd.core.platform.monitor.Monitor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
@Monitor("exceptions")
@XmlRootElement(name = "exceptions")
public class ExceptionMonitor {
    private final Map<Class<?>, ExceptionCounter> exceptionCounters = new HashMap<>();

    int errors;

    int warnings;

    public void error(Throwable e) {
        errors++;

        countException(e);
    }

    public void warn(Throwable e) {
        warnings++;

        countException(e);
    }

    private void countException(Throwable e) {
        ExceptionCounter exceptionInfoCounter = exceptionCounters.get(e.getClass());

        if (exceptionInfoCounter == null) {
            exceptionInfoCounter = createExceptionCounter(e);
        }

        exceptionInfoCounter.increase();
    }

    ExceptionCounter createExceptionCounter(Throwable e) {
        ExceptionCounter exceptionCounter = new ExceptionCounter();
        exceptionCounter.setExceptionClass(e.getClass().getName());

        exceptionCounters.put(e.getClass(), exceptionCounter);

        return exceptionCounter;
    }

    @XmlElement(name = "errors")
    public int getErrors() {
        return errors;
    }

    @XmlElement(name = "warnings")
    public int getWarnings() {
        return warnings;
    }

    @XmlElementWrapper(name = "exceptions")
    @XmlElement(name = "exception")
    public List<ExceptionCounter> getExceptionCounters() {
        return new ArrayList<>(exceptionCounters.values());
    }
}
