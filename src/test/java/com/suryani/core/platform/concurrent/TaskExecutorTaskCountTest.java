package com.zyd.core.platform.concurrent;

import com.zyd.core.platform.SpringObjectFactory;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.easymock.EasyMock.expect;

/**
 * @author neo
 */
public class TaskExecutorTaskCountTest extends EasyMockSupport {
    SpringObjectFactory springObjectFactory;
    TaskExecutor taskExecutor;

    @Before
    public void createTaskExecutor() {
        springObjectFactory = createMock(SpringObjectFactory.class);

        taskExecutor = TaskExecutor.unlimitedExecutor();
        taskExecutor.setSpringObjectFactory(springObjectFactory);
    }

    @After
    public void cleanTaskExecutor() {
        taskExecutor.shutdown();
    }

    @Test
    public void getActiveTaskCount() {
        TestTask task = new TestTask();
        expect(springObjectFactory.initializeBean(task)).andReturn(task);
        replayAll();

        List<TestTask> tasks = new ArrayList<>();
        tasks.add(task);

        taskExecutor.executeAll(tasks);

        verifyAll();
    }

    public class TestTask implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            Assert.assertEquals(1, taskExecutor.getActiveTaskCount());
            return true;
        }
    }
}
