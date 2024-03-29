package com.zyd.core.platform.monitor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class StatusTest {
    Status status;

    @Before
    public void createStatus() {
        status = new Status();
    }

    @Test
    public void toXML() {
        ServiceDetail dbStatus = new ServiceDetail();
        dbStatus.setStatus(ServiceStatus.UP);
        dbStatus.setElapsedTime(200);
        status.serviceDetails.put("db", dbStatus);

        String xml = status.toXML(new ServerStatus());

        assertThat(xml, containsString("<server>UP</server>"));
        assertThat(xml, containsString("<service name=\"db\">"));
    }
}
