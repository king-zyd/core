package com.zyd.core.platform.web.rest.api;

import com.zyd.core.SpringServiceTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Raymond
 */
public class APIScannerTest extends SpringServiceTest {
    @Inject
    Environment env;

    @Test
    public void testScan() throws Exception {
        APIScanner apiScanner = new APIScanner(getClass().getPackage().getName());
        List<Class<?>> classes = apiScanner.scan();
        Assert.assertTrue(!classes.isEmpty());
    }
}
