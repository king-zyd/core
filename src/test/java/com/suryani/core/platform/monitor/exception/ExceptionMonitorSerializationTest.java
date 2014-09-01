package com.zyd.core.platform.monitor.exception;

import com.zyd.core.xml.XMLBinder;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author Chi
 */
public class ExceptionMonitorSerializationTest {
    @Test
    public void serializeToXML() {
        ExceptionMonitor monitor = new ExceptionMonitor();
        monitor.error(new RuntimeException());

        String xml = XMLBinder.binder(ExceptionMonitor.class).toXML(monitor);
        assertThat(xml, containsString("<errors>1</errors>"));
    }
}
