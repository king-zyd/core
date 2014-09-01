package com.zyd.core.platform;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;

/**
 * @author chi
 */
public class DefaultAppContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {
    public void initialize(ConfigurableWebApplicationContext context) {
        try {
            context.getEnvironment().setIgnoreUnresolvableNestedPlaceholders(true);

            Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:*.properties");
            MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
            for (Resource resource : resources) {
                if (!propertySources.contains(resource.getFilename())) {
                    propertySources.addLast(new ResourcePropertySource(resource.getFilename(), resource));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}