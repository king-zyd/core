package com.zyd.core.platform.concurrent;

import com.zyd.core.SpringServiceTest;
import com.zyd.core.log.TraceLogger;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author neo
 */
public class TaskExecutorTest extends SpringServiceTest {
    private final Logger logger = LoggerFactory.getLogger(TaskExecutorTest.class);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Inject
    TaskExecutor taskExecutor;

    @Before
    public void initTraceLogger() {
        TraceLogger.get().initialize();
        logger.debug("start test");
    }

    @After
    public void cleanTraceLogger() {
        logger.debug("end test");
        TraceLogger.get().cleanup(false);
    }

    @Test
    public void executeAll() {
        List<TestTask> tasks = new ArrayList<TestTask>();
        tasks.add(new TestTask("1", false));
        tasks.add(new TestTask("2", false));
        tasks.add(new TestTask("3", false));

        List<String> results = taskExecutor.executeAll(tasks);

        Assert.assertEquals(3, results.size());
        Assert.assertTrue(results.contains("1"));
        Assert.assertTrue(results.contains("2"));
        Assert.assertTrue(results.contains("3"));
    }

    @Test
    public void failedToExecuteAll() {
        exception.expect(TaskExecutionException.class);

        List<TestTask> tasks = new ArrayList<TestTask>();
        tasks.add(new TestTask("1", false));
        tasks.add(new TestTask("2", true));
        tasks.add(new TestTask("3", false));

        taskExecutor.executeAll(tasks);
    }

    public static class TestTask implements Callable<String> {
        private final Logger logger = LoggerFactory.getLogger(TestTask.class);
        private String result;
        private boolean willFail;

        public TestTask(String result, boolean willFail) {
            this.result = result;
            this.willFail = willFail;
        }

        @Override
        public String call() throws Exception {
            logger.debug("within test task");
            if (willFail) {
                logger.error("test failure");
                throw new RuntimeException("failed");
            }
            return result;
        }
    }
}
