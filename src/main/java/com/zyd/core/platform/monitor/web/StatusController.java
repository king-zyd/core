package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.monitor.ServerStatus;
import com.zyd.core.platform.monitor.ServiceMonitor;
import com.zyd.core.platform.monitor.Status;
import com.zyd.core.platform.web.rest.RESTController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * @author neo
 */
@Controller
public class StatusController extends RESTController {
    private SpringObjectFactory objectFactory;
    private ServerStatus serverStatus;

    @RequestMapping(value = "/monitor/status", produces = "application/xml", method = RequestMethod.GET)
    @ResponseBody
    public String status() {
        Status status = new Status();
        List<ServiceMonitor> monitors = objectFactory.getBeans(ServiceMonitor.class);
        status.check(monitors);
        return status.toXML(serverStatus);
    }

    @Inject
    public void setObjectFactory(SpringObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Inject
    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
}
