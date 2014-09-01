package com.zyd.core.platform.management.web;

import com.zyd.core.log.LogMessageFilter;
import com.zyd.core.log.LogSettings;
import com.zyd.core.platform.management.web.view.LogSettingsView;
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
public class LogManagementController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(LogManagementController.class);
    private RequestContext requestContext;
    private LogSettings logSettings;

    @RequestMapping(value = "/management/log", method = RequestMethod.GET)
    @ResponseBody
    public LogSettingsView get() {
        return createView(logSettings);
    }

    @RequestMapping(value = "/management/log", method = RequestMethod.PUT)
    @ResponseBody
    public LogSettingsView update(@RequestParam(value = "enableTraceLog", required = false, defaultValue = "true") boolean enableTraceLog,
                                  @RequestParam(value = "alwaysWriteTraceLog", required = false, defaultValue = "false") boolean alwaysWriteTraceLog) {
        logger.info("updated log config, enableTraceLog={}, alwaysWriteTraceLog={}, updatedBy={}", enableTraceLog, alwaysWriteTraceLog, requestContext.getRemoteAddress());
        logSettings.setEnableTraceLog(enableTraceLog);
        logSettings.setAlwaysWriteTraceLog(alwaysWriteTraceLog);
        return createView(logSettings);
    }

    private LogSettingsView createView(LogSettings logSettings) {
        LogSettingsView view = new LogSettingsView();
        view.setEnableTraceLog(logSettings.isEnableTraceLog());
        view.setAlwaysWriteTraceLog(logSettings.isAlwaysWriteTraceLog());
        LogMessageFilter messageFilter = logSettings.getLogMessageFilter();
        if (messageFilter != null) {
            view.setMessageFilterClass(messageFilter.getClass().getName());
        }
        return view;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setLogSettings(LogSettings logSettings) {
        this.logSettings = logSettings;
    }
}
