package com.zyd.core;

import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.IOUtils;

import java.io.InputStream;

/**
 * @author neo
 */
public class TestResource {
    public static byte[] bytes(String testResourcePath) {
        InputStream stream = TestResource.class.getResourceAsStream(testResourcePath);
        AssertUtils.assertNotNull(stream, "resource does not exist, resource=" + testResourcePath);
        return IOUtils.bytes(stream);
    }

    public static String text(String testResourcePath) {
        InputStream stream = TestResource.class.getResourceAsStream(testResourcePath);
        AssertUtils.assertNotNull(stream, "resource does not exist, resource=" + testResourcePath);
        return IOUtils.text(stream);
    }
}
