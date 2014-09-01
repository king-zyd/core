package com.zyd.core.platform;

import com.zyd.core.SpringServiceTest;
import com.suryani.rest.platform.SpringObjectFactoryTestBean;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class SpringObjectFactoryTest extends SpringServiceTest {
    @Inject
    SpringObjectFactory objectFactory;

    @Test
    public void initializeBeanShouldApplyAOPInterceptor() {
        SpringObjectFactoryTestBean bean = new SpringObjectFactoryTestBean();
        SpringObjectFactoryTestBean beanProxy1 = objectFactory.initializeBean(bean);
        beanProxy1.verify();
        Assert.assertNotSame(beanProxy1, bean);
        Assert.assertFalse(SpringObjectFactoryTestBean.class.equals(beanProxy1.getClass()));
    }

    @Test
    public void initializeBeanShouldCreatePrototype() {
        SpringObjectFactoryTestBean bean = new SpringObjectFactoryTestBean();
        SpringObjectFactoryTestBean beanProxy1 = objectFactory.initializeBean(bean);
        SpringObjectFactoryTestBean beanProxy2 = objectFactory.initializeBean(bean);
        Assert.assertNotSame(beanProxy1, beanProxy2);
    }

    public static class TestRegistrationBean {

    }

    @Test
    public void registerBean() {
        objectFactory.registerSingletonBean("testRegistrationBean", TestRegistrationBean.class);
        TestRegistrationBean bean = objectFactory.getBean(TestRegistrationBean.class);

        Assert.assertNotNull(bean);
    }
}
