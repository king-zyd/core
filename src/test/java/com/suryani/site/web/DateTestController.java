package com.suryani.site.web;

import com.zyd.core.util.Convert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * User: Lance.zhou
 * Date: 12/13/13
 */
@Controller
public class DateTestController {

    @RequestMapping(value = "/test/date-simple")
    @ResponseBody
    public String simpleArguments(@RequestParam(value = "date") Date date) {
        return Convert.toString(date, Convert.DATE_FORMAT_DATETIME);
    }

    @RequestMapping(value = "/test/date-form")
    @ResponseBody
    public String formArguments(DateForm form) {
        return Convert.toString(form.getDate(), Convert.DATE_FORMAT_DATETIME);
    }

    @RequestMapping(value = "/test/date-json")
    @ResponseBody
    public DateForm jsonArguments(@RequestBody DateForm form) {
        return form;
    }

    public static class DateForm {
        Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
