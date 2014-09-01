package com.zyd.core.platform.monitor.performance;

import com.zyd.core.xml.XMLBuilder;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author neo
 */
public class PerformanceStatistics {
    private final Map<String, PerformanceStatistic> statistics = new ConcurrentHashMap<>();

    public void record(String action, double elapsedTime, Date executionDate) {
        PerformanceStatistic statistic = statistics.get(action);
        if (statistic == null) {
            statistic = new PerformanceStatistic();
            statistics.put(action, statistic);
        }
        statistic.record(elapsedTime, executionDate);
    }

    public String toXML() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        builder.startElement("performanceStatistic");
        for (Map.Entry<String, PerformanceStatistic> entry : statistics.entrySet()) {
            builder.startElement("action");
            builder.attribute("name", entry.getKey());

            entry.getValue().toXML(builder);

            builder.endElement();
        }
        builder.endElement();   // end of performanceStatistic
        return builder.toXML();
    }
}
