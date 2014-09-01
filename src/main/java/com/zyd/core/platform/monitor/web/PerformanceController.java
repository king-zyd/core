package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.monitor.performance.PerformanceStatistics;
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
public class PerformanceController extends RESTController {
    private PerformanceStatistics performanceStatistics;

    @RequestMapping(value = "/monitor/performance", produces = "application/xml", method = RequestMethod.GET)
    @ResponseBody
    public String performanceStatistic() {
        return performanceStatistics.toXML();
    }

    @Inject
    public void setPerformanceStatistics(PerformanceStatistics performanceStatistics) {
        this.performanceStatistics = performanceStatistics;
    }
}
