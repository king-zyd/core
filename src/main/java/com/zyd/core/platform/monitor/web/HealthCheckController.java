package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.web.rest.RESTController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * with aws the health check url requires to expose to ELB which to outside, we don't want to expose /monitor/*
 * <p/>
 * so to create safe health check url
 *
 * @author neo
 */
@Controller
public class HealthCheckController extends RESTController {
    @RequestMapping(value = "/health-check", method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseBody
    public void healthCheck() {
    }
}
