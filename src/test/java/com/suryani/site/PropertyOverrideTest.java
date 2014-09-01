package com.suryani.site;

import com.zyd.core.SpringSiteTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * User: Lance.zhou
 * Date: 10/28/13
 */
public class PropertyOverrideTest extends SpringSiteTest {

    @Inject
    Environment env;

    @Value("${test.key_override}")
    String prop;

    @Test
    public void shouldUsePropertyUnderTestResource() {
        Assert.assertEquals("overridden value", env.getRequiredProperty("test.key_override"));
        Assert.assertEquals("overridden value", prop);
    }
}
