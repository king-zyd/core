package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.runtime.RuntimeSettings;
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
public class RuntimeInfoController extends RESTController {
    private RuntimeSettings runtimeSettings;

    @RequestMapping(value = "/monitor/runtime", method = RequestMethod.GET)
    @ResponseBody
    public RuntimeSettings runtimeSettings() {
        return runtimeSettings;
    }

    @Inject
    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }
}
