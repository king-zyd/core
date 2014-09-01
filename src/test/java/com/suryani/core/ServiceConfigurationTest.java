package com.zyd.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ServiceConfigurationTest extends SpringServiceTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    public void contextShouldBeInitialized() {
        Assert.assertNotNull(applicationContext);
    }

    @Test
    public void verifyBeanConfiguration() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            applicationContext.getBean(beanName);
        }
    }
}
