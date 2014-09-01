package com.zyd.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class MathUtilsTest {
    @Test
    public void roundToNearestOne() {
        Assert.assertEquals(1000, MathUtils.round(1000.1, 0), 0);
        Assert.assertEquals(1001, MathUtils.round(1000.5, 0), 0);
    }

    @Test
    public void roundToNearestHundredth() {
        Assert.assertEquals(1000.11, MathUtils.round(1000.111, 2), 0);
        Assert.assertEquals(1000.12, MathUtils.round(1000.115, 2), 0);
    }
}
