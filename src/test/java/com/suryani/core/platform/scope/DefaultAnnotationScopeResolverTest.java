package com.zyd.core.platform.scope;

import com.zyd.core.SpringServiceTest;
import com.suryani.rest.platform.ScopeTestSingletonBean;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class DefaultAnnotationScopeResolverTest extends SpringServiceTest {
    @Inject
    ScopeTestSingletonBean singleton1;
    @Inject
    ScopeTestSingletonBean singleton2;

    @Test
    public void singletonScopeIsSupported() {
        Assert.assertSame(singleton1, singleton2);
    }
}
