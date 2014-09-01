package com.suryani.site;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User: Leon.wu
 * Date: 10/21/13
 */
@Component
public class BeanWithPropertyPlaceholder {
    String testKey;

    @Value("${testKey}")
    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }
}
