package com.suryani.rest.platform.web;

import com.zyd.core.platform.web.rest.RESTController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author neo
 */
@Controller
public class ErrorTestRESTController extends RESTController {
    @RequestMapping(value = "/test/method-not-allowed", produces = "application/xml", method = RequestMethod.POST)
    @ResponseBody
    public String onlyAllowPOST() {
        return "<result/>";
    }
}
