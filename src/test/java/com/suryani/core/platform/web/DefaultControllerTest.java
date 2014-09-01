package com.zyd.core.platform.web;

import com.zyd.core.SpringServiceTest;
import com.zyd.core.platform.SpringObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class DefaultControllerTest extends SpringServiceTest {
    @Inject
    SpringObjectFactory springObjectFactory;

    DefaultController defaultController;

    @Before
    public void createDefaultController() {
        defaultController = springObjectFactory.createBean(DefaultController.class);
    }

    @Test
    public void getMessage() {
        String message = defaultController.getMessage("test.message1");
        Assert.assertEquals("testMessage", message);
    }

    @Test
    public void getMessageWithParams() {
        String message = defaultController.getMessage("test.message2", "Value");
        Assert.assertEquals("testMessageValue", message);
    }
}
