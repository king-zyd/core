package com.zyd.core.platform.web.rest.api;

import com.zyd.core.platform.web.rest.client.RESTServiceClient;
import com.zyd.core.util.AssertUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;

/**
 * @author Raymond
 */
public class APIProxyCreator {
    private final String serverURL;
    private final Class<?> apiClass;
    private final RESTServiceClient restServiceClient;

    public APIProxyCreator(String serverURL, Class<?> apiClass, RESTServiceClient restServiceClient) {
        this.serverURL = serverURL;
        this.apiClass = apiClass;
        this.restServiceClient = restServiceClient;
    }

    public Object create() {
        return Enhancer.create(apiClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return doCreate(serverURL, method, args);
            }
        });
    }

    Object doCreate(String interfaceUri, Method method, Object[] args) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        AssertUtils.assertNotNull(requestMapping, " request mapping is required.");
        AssertUtils.assertTrue(requestMapping.method().length > 0, " request method is required.");

        if (requestMapping.method()[0] == RequestMethod.GET) {

            String uri = args.length <= 0 ? requestMapping.value()[0] : APIUriUtils.formatPathVariable(requestMapping.value()[0], method.getParameterAnnotations(), args);
            return restServiceClient.get(APIUriUtils.buildUri(interfaceUri, uri), method.getReturnType());

        } else if (requestMapping.method()[0] == RequestMethod.POST) {
            return restServiceClient.post(APIUriUtils.buildUri(interfaceUri, requestMapping.value()[0]), method.getReturnType(), args[0]);

        } else if (requestMapping.method()[0] == RequestMethod.PUT) {
            return restServiceClient.put(APIUriUtils.buildUri(interfaceUri, requestMapping.value()[0]), method.getReturnType(), args[0]);

        } else if (requestMapping.method()[0] == RequestMethod.DELETE) {

            String uri = args.length <= 0 ? requestMapping.value()[0] : APIUriUtils.formatPathVariable(requestMapping.value()[0], method.getParameterAnnotations(), args);
            return restServiceClient.delete(APIUriUtils.buildUri(interfaceUri, uri), method.getReturnType());

        } else {
            throw new IllegalStateException("the method:" + requestMapping.method()[0].toString() + " does not support.");
        }
    }

}
