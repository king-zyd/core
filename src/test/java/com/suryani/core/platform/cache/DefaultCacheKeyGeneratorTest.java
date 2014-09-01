package com.zyd.core.platform.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class DefaultCacheKeyGeneratorTest {
    DefaultCacheKeyGenerator defaultCacheKeyGenerator;

    @Before
    public void createDefaultCacheKeyGenerator() {
        defaultCacheKeyGenerator = new DefaultCacheKeyGenerator();
    }

    @Test
    public void encodeKeyIfContainsIllegalChar() {
        String encodedKey = defaultCacheKeyGenerator.encodeKey("contains space key");
        Assert.assertEquals("illegal key should be encoded by md5, which is 32 chars long", 32, encodedKey.length());
    }
}
