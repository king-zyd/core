package com.zyd.core.platform.monitor;

import com.zyd.core.util.StopWatch;
import com.zyd.core.xml.XMLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
public class Status {
    private final Logger logger = LoggerFactory.getLogger(Status.class);

    Map<String, ServiceDetail> serviceDetails = new HashMap<String, ServiceDetail>();

    public void check(List<ServiceMonitor> monitors) {
        for (ServiceMonitor monitor : monitors) {
            check(monitor);
        }
    }

    private void check(ServiceMonitor monitor) {
        StopWatch watch = new StopWatch();

        ServiceDetail detail = new ServiceDetail();
        try {
            ServiceStatus status = monitor.getServiceStatus();
            detail.setStatus(status);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            detail.setErrorMessage(e.getClass().getName() + " " + e.getMessage());
            detail.setStatus(ServiceStatus.DOWN);
        } finally {
            detail.setElapsedTime(watch.elapsedTime());
        }

        serviceDetails.put(monitor.getServiceName(), detail);
    }

    public String toXML(ServerStatus serverStatus) {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        builder.startElement("status");
        builder.textElement("server", serverStatus.status().name());

        builder.startElement("services");
        for (Map.Entry<String, ServiceDetail> entry : serviceDetails.entrySet()) {
            builder.startElement("service");
            builder.attribute("name", entry.getKey());
            ServiceDetail detail = entry.getValue();
            builder.textElement("status", detail.getStatus().name());
            builder.textElement("elapsedTime", String.valueOf(detail.getElapsedTime()));
            builder.textElement("errorMessage", detail.getErrorMessage());
            builder.endElement();
        }
        builder.endElement();   // end of services

        builder.endElement();   // end of status

        return builder.toXML();
    }
}
