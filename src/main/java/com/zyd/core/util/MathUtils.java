package com.zyd.core.util;

/**
 * @author neo
 */
public final class MathUtils {
    public static double round(double number, int digits) {
        double scale = Math.pow(10, digits);
        return Math.round(number * scale) / scale;
    }

    private MathUtils() {
    }
}
