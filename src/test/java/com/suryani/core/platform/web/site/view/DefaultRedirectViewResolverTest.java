package com.zyd.core.platform.web.site.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class DefaultRedirectViewResolverTest {
    DefaultRedirectViewResolver defaultRedirectViewResolver;

    @Before
    public void createRedirectViewResolver() {
        defaultRedirectViewResolver = new DefaultRedirectViewResolver();
    }

    @Test
    public void shouldNotUseSpringRelativeRedirect() {
        Assert.assertFalse(defaultRedirectViewResolver.useSpringRelativeRedirect());
    }

    @Test
    public void shouldNotUseHTTP10CompatibleRedirect() {
        Assert.assertFalse(defaultRedirectViewResolver.useHTTP10Redirect());
    }
}
