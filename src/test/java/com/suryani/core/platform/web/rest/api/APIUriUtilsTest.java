package com.zyd.core.platform.web.rest.api;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * @author Raymond
 */
public class APIUriUtilsTest {
    @Test
    public void testBuildUri() throws Exception {
        String expectedUri = "localhost:8080/enable-api/get";
        Assert.assertEquals(expectedUri, APIUriUtils.buildUri("localhost:8080/enable-api/", "/get"));
        Assert.assertEquals(expectedUri, APIUriUtils.buildUri("localhost:8080/enable-api/", "get"));
        Assert.assertEquals(expectedUri, APIUriUtils.buildUri("localhost:8080/enable-api", "/get"));
        Assert.assertEquals(expectedUri, APIUriUtils.buildUri("localhost:8080/enable-api", "get"));
    }

    @Test
    public void testFormatPathVariable() throws Exception {
        Class<?> clazz = TestInterface.class;
        Method get = clazz.getDeclaredMethod("get", String.class);
        RequestMapping requestMapping = get.getAnnotation(RequestMapping.class);
        Object[] args = new Object[1];
        args[0] = "1";

        String action = APIUriUtils.formatPathVariable(requestMapping.value()[0], get.getParameterAnnotations(), args);
        Assert.assertEquals("/enable-api/get/1", action);

        args = new Object[]{"1", "2"};
        Method delete = clazz.getDeclaredMethod("delete", String.class, String.class);
        requestMapping = delete.getAnnotation(RequestMapping.class);
        action = APIUriUtils.formatPathVariable(requestMapping.value()[0], delete.getParameterAnnotations(), args);
        Assert.assertEquals("/enable-api/1/delete/2", action);
    }

    interface TestInterface {
        @RequestMapping(value = "/enable-api/get/{variable}", method = RequestMethod.GET)
        @ResponseBody
        Object get(@PathVariable("variable") String id);

        @RequestMapping(value = "/enable-api/{variable1}/delete/{variable2}", method = RequestMethod.GET)
        @ResponseBody
        Object delete(@PathVariable("variable1") String variable1, @PathVariable("variable2") String variable2);
    }
}
