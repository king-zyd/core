package com.zyd.core.platform;

import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;

import static org.easymock.EasyMock.expect;

/**
 * @author neo
 */
public class PlatformScopeResolverTest extends EasyMockSupport {
    PlatformScopeResolver platformScopeResolver;
    AnnotationMetadata metadata;

    @Before
    public void createPlatformScopeResolver() {
        platformScopeResolver = new PlatformScopeResolver();

        metadata = createNiceMock(AnnotationMetadata.class);
    }

    @Test
    public void configurationAnnotationShouldBeRecognizedAsSingleton() {
        expect(metadata.isAnnotated(Configuration.class.getName())).andReturn(true);
        replayAll();

        Assert.assertTrue(platformScopeResolver.annotatedAsSingleton(metadata));
        verifyAll();
    }
}
