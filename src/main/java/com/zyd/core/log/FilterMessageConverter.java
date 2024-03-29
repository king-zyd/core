package com.zyd.core.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author neo
 * @see ch.qos.logback.classic.pattern.MessageConverter
 */
public class FilterMessageConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        return filter(event.getLoggerName(), message);
    }

    private String filter(String loggerName, String message) {
        LogMessageFilter filter = LogSettings.get().getLogMessageFilter();
        if (filter != null) {
            return filter.filter(loggerName, message);
        }
        return message;
    }
}
