package com.zyd.core.platform.web.request;

import com.zyd.core.log.ActionLoggerImpl;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

/**
 * @author neo
 */
public class RequestContextInterceptorTest extends EasyMockSupport {
    RequestContextInterceptor requestContextInterceptor;
    ActionLoggerImpl actionLogger;
    RequestContextImpl requestContext;

    @Before
    public void createRequestContextInterceptor() {
        actionLogger = createMock(ActionLoggerImpl.class);
        requestContext = new RequestContextImpl();

        requestContextInterceptor = new RequestContextInterceptor();
        requestContextInterceptor.setActionLogger(actionLogger);
        requestContextInterceptor.setRequestContext(requestContext);
    }

    public static class TestController {
        public String execute() {
            return "view";
        }
    }

    @Test
    public void assignActionWithFirstHandlerMethod() throws NoSuchMethodException {
        String expectedAction = "RequestContextInterceptorTest$TestController-execute";
        actionLogger.setCurrentAction(expectedAction);

        replayAll();

        requestContextInterceptor.assignAction(new HandlerMethod(new TestController(), TestController.class.getMethod("execute")));
        Assert.assertEquals(expectedAction, requestContext.getAction());

        verifyAll();
    }

    @Test
    public void assignActionWhenAssignedAlready() throws NoSuchMethodException {
        requestContext.setAction("assigned-action");
        replayAll();

        requestContextInterceptor.assignAction(new HandlerMethod(new TestController(), TestController.class.getMethod("execute")));
        Assert.assertEquals("assigned-action", requestContext.getAction());

        verifyAll();
    }
}
