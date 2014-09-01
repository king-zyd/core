package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.monitor.MonitorManager;
import com.zyd.core.platform.monitor.web.view.MonitorBeans;
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
public class MonitorController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private MonitorManager monitorManager;

    @RequestMapping(value = "/monitor/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Object monitored(@PathVariable("name") String name) {
        logger.debug("get statistics {}", name);
        Object monitorBean = monitorManager.monitorBean(name);

        if (monitorBean == null) {
            throw new ResourceNotFoundException(String.format("Monitor object not found %s", name));
        }

        return monitorBean;
    }

    @RequestMapping(value = "/monitors", method = RequestMethod.GET)
    @ResponseBody
    public MonitorBeans beans() {
        MonitorBeans names = new MonitorBeans();

        for (String name : monitorManager.monitorBeanNames()) {
            names.getNames().add(name);
        }

        return names;
    }

    @Inject
    public void setMonitorManager(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }
}
