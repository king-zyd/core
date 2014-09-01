package com.zyd.core.platform.management.web;

import com.zyd.core.platform.monitor.ServerStatus;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.rest.RESTController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author neo
 */
@Controller
public class ServerStatusManagementController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(ServerStatusManagementController.class);
    private RequestContext requestContext;
    private ServerStatus serverStatus;

    @RequestMapping(value = "/management/server-status", method = RequestMethod.GET)
    @ResponseBody
    public ServerStatus get() {
        return serverStatus;
    }

    @RequestMapping(value = "/management/server-status", method = RequestMethod.PUT)
    @ResponseBody
    public ServerStatus update(@RequestParam(value = "disable") boolean disable) {
        logger.info("updated server-status, disable={}, updatedBy={}", disable, requestContext.getRemoteAddress());
        if (disable) {
            serverStatus.disable();
        } else {
            serverStatus.enable();
        }
        return serverStatus;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
}
