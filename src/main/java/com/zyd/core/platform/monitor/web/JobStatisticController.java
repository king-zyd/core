package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.scheduler.info.JobStatistic;
import com.zyd.core.platform.web.rest.RESTController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author neo
 */
@Controller
public class JobStatisticController extends RESTController {
    private JobStatistic jobStatistic;

    @RequestMapping(value = "/monitor/jobs", produces = "application/xml", method = RequestMethod.GET)
    @ResponseBody
    public String jobs() {
        return jobStatistic.toXML();
    }

    @Inject
    public void setJobStatistic(JobStatistic jobStatistic) {
        this.jobStatistic = jobStatistic;
    }
}
