package com.suryani.site;

import com.zyd.core.SpringSiteTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * User: Leon.wu
 * Date: 10/21/13
 */
public class EnvironmentPropertyTest extends SpringSiteTest {
    @Inject
    BeanWithPropertyPlaceholder beanWithPropertyPlaceholder;

    @Test
    public void shouldInjectValueForPropertyPlaceholder() {
        Assert.assertEquals("testValue", beanWithPropertyPlaceholder.testKey);
    }

}
