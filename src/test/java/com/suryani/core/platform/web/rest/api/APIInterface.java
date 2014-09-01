package com.zyd.core.platform.web.rest.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Raymond
 */
@API
public interface APIInterface {
    @RequestMapping(value = "/enable-api/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    Object get(@PathVariable("id") String gcCode);

    @RequestMapping(value = "/enable-api/list", method = RequestMethod.GET)
    @ResponseBody
    Object list();

    @RequestMapping(value = "/enable-api/post", method = RequestMethod.POST)
    @ResponseBody
    Object post(Object object);

    @RequestMapping(value = "/enable-api/put", method = RequestMethod.PUT)
    @ResponseBody
    Object put(Object object);

    @RequestMapping(value = "enable-api/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") String gcCode);

}
