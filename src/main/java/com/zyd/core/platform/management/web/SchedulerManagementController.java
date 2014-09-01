package com.zyd.core.platform.management.web;

import com.zyd.core.platform.scheduler.SchedulerImpl;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.rest.RESTController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author neo
 */
@Controller
public class SchedulerManagementController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(SchedulerManagementController.class);
    private RequestContext requestContext;
    private SchedulerImpl scheduler;

    @RequestMapping(value = "/management/scheduler/job/{jobId}", method = RequestMethod.PUT)
    @ResponseBody
    public String update(@PathVariable("jobId") String jobId) {
        logger.info("trigger job, jobId={}, updatedBy={}", jobId, requestContext.getRemoteAddress());
        scheduler.triggerJobOnce(jobId);
        return "job triggered, jobId=" + jobId;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setScheduler(SchedulerImpl scheduler) {
        this.scheduler = scheduler;
    }
}
