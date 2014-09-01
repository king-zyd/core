package com.zyd.core.platform.scheduler.info;

import com.zyd.core.platform.ClassUtils;
import com.zyd.core.platform.scheduler.Job;
import com.zyd.core.xml.XMLBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 */
public class JobStatistic {
    private final Map<String, JobInfo> jobs = new HashMap<>();

    public void registerJob(String jobId, Class<? extends Job> jobClass, String description) {
        JobInfo info = new JobInfo();
        info.setJobId(jobId);
        info.setJobClass(ClassUtils.getOriginalClassName(jobClass));
        info.setDescription(description);
        jobs.put(jobId, info);
    }

    public void start(String jobId) {
        JobInfo jobInfo = jobs.get(jobId);
        jobInfo.setLastStarted(new Date());
    }

    public void succeed(String jobId) {
        JobInfo jobInfo = jobs.get(jobId);
        jobInfo.setLastFinished(new Date());
        jobInfo.setLastResult("success");
    }

    public void failed(String jobId) {
        JobInfo jobInfo = jobs.get(jobId);
        jobInfo.setLastFinished(new Date());
        jobInfo.setLastResult("failed");
    }

    public String toXML() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        builder.startElement("job_statistics");
        for (JobInfo info : jobs.values()) {
            builder.startElement("job");
            builder.attribute("id", info.getJobId());
            builder.attribute("class", info.getJobClass());
            builder.attribute("description", info.getDescription());
            builder.attribute("last_result", info.getLastResult());
            builder.attribute("last_started", info.getLastStartedText());
            builder.attribute("last_finished", info.getLastFinishedText());
            builder.endElement();
        }
        builder.endElement(); // end of job_statistics
        return builder.toXML();
    }

    public Map<String, JobInfo> getJobs() {
        return jobs;
    }

}
