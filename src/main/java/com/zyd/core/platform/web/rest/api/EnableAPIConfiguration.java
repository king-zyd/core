package com.zyd.core.platform.web.rest.api;

import com.zyd.core.http.HTTPClient;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.web.rest.client.RESTServiceClient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import javax.inject.Inject;

import java.util.List;

/**
 * @author Raymond
 */
@Configuration
public class EnableAPIConfiguration implements ImportAware {
    private final Logger logger = LoggerFactory.getLogger(EnableAPIConfiguration.class);

    @Inject
    SpringObjectFactory springObjectFactory;

    @Inject
    Environment env;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes enableAPI = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableAPI.class.getName(), false));
        Assert.notNull(enableAPI, "@EnableAPI is not present on importing class " + importMetadata.getClassName());

        RESTServiceClient serviceClient = this.createRESTServiceClient(enableAPI);

        try {
            String serverURL = env.resolveRequiredPlaceholders(enableAPI.getString("serverURL"));
            APIScanner apiScanner = new APIScanner(env.resolveRequiredPlaceholders(enableAPI.getString("scanPackage")));
            List<Class<?>> apiClasses = apiScanner.scan();

            for (Class<?> apiClass : apiClasses) {
                logger.info("register api service {}", apiClass);
                this.registerAPIService(apiClass, new APIProxyCreator(serverURL, apiClass, serviceClient).create());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RESTServiceClient createRESTServiceClient(AnnotationAttributes enableAPI) {
        RESTServiceClient serviceClient = new RESTServiceClient();
        serviceClient.setHTTPClient(springObjectFactory.getBean(HTTPClient.class));

        String clientId = env.resolveRequiredPlaceholders(enableAPI.getString("clientId"));
        if (!StringUtils.isEmpty(clientId)) {
            serviceClient.setClientSignatureKey(clientId, env.resolveRequiredPlaceholders(enableAPI.getString("clientKey")));
        }

        return serviceClient;
    }

    private void registerAPIService(Class<?> apiType, Object apiProxyObject) {
        springObjectFactory.registerSingletonBean(apiType.getName(), apiProxyObject);
    }
}
