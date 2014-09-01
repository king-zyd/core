package com.suryani.rest.platform;

import org.junit.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author neo
 */
@Service
public class SpringObjectFactoryTestBean {
    private SpringObjectFactoryTestDependency dependency;

    @Transactional
    public void verify() {
        Assert.assertNotNull(dependency);
    }

    @Inject
    public void setDependency(SpringObjectFactoryTestDependency dependency) {
        this.dependency = dependency;
    }
}
