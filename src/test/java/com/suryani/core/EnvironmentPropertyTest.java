package com.zyd.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author neo
 */
public class EnvironmentPropertyTest extends SpringServiceTest {
    @Inject
    Environment environment;

    @Test
    public void ignoreUnresolvableNestedPlaceholders() {
        String value = environment.getRequiredProperty("testNestedPlaceholderKey");
        Assert.assertEquals("${testValue}", value);
    }
}
