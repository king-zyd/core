package com.zyd.core.platform.monitor.performance;

import com.zyd.core.util.Convert;
import com.zyd.core.xml.XMLBuilder;

import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author neo
 */
public class PerformanceStatistic {
    private static final int MAX_COUNT_SLOW_EXECUTIONS = 5;

    double totalElapsedTime;
    long totalExecutions;
    final SortedSet<ExecutionInfo> topSlowestExecutions = new ConcurrentSkipListSet<>(new Comparator<ExecutionInfo>() {
        @Override
        public int compare(ExecutionInfo info1, ExecutionInfo info2) {
            double elapsedTime1 = info1.getElapsedTime();
            double elapsedTime2 = info2.getElapsedTime();
            if (elapsedTime1 == elapsedTime2) return info1.getExecutionDate().compareTo(info2.getExecutionDate());
            return -Double.compare(elapsedTime1, elapsedTime2);
        }
    });

    public void record(double elapsedTime, Date executionDate) {
        totalElapsedTime += elapsedTime;
        totalExecutions++;

        if (topSlowestExecutions.size() < getMaxCountOfSlowExecutions() || topSlowestExecutions.last().getElapsedTime() < elapsedTime) {
            ExecutionInfo executionInfo = new ExecutionInfo();
            executionInfo.setElapsedTime(elapsedTime);
            executionInfo.setExecutionDate(executionDate);
            topSlowestExecutions.add(executionInfo);

            if (topSlowestExecutions.size() > getMaxCountOfSlowExecutions()) {
                topSlowestExecutions.remove(topSlowestExecutions.last());
            }
        }
    }

    int getMaxCountOfSlowExecutions() {
        return MAX_COUNT_SLOW_EXECUTIONS;
    }

    public double getAverageElapsedTime() {
        return Math.round(totalElapsedTime / totalExecutions);
    }

    public void toXML(XMLBuilder builder) {
        builder.textElement("totalCount", String.valueOf(totalExecutions));
        builder.textElement("averageElapsedTimeInMs", String.valueOf(getAverageElapsedTime()));
        builder.startElement("topSlowExecutions");
        for (ExecutionInfo execution : topSlowestExecutions) {
            builder.startElement("execution");
            builder.attribute("date", Convert.toString(execution.getExecutionDate(), Convert.DATE_FORMAT_DATETIME));
            builder.attribute("elapsedTimeInMs", String.valueOf(execution.getElapsedTime()));
            builder.endElement();
        }
        builder.endElement();   // end of topSlowExecutions
    }
}
