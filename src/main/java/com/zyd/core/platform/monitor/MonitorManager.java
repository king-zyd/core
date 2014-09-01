package com.zyd.core.platform.monitor;

import com.zyd.core.platform.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chi
 */
public class MonitorManager implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(MonitorManager.class);
    private final Map<String, Object> monitorBeans = new HashMap<>();

    public Object monitorBean(String name) {
        return monitorBeans.get(name);
    }

    public List<String> monitorBeanNames() {
        return new ArrayList<>(monitorBeans.keySet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<Object> monitorBeans = findMonitorBeans(applicationContext);

        for (Object monitorBean : monitorBeans) {
            Monitor monitor = ClassUtils.getOriginalClass(monitorBean.getClass()).getAnnotation(Monitor.class);

            if (monitor != null) {
                logger.debug("find monitor object, {}={}", monitor.value(), monitorBean);
                this.monitorBeans.put(monitor.value(), monitorBean);
            }
        }
    }

    List<Object> findMonitorBeans(ApplicationContext applicationContext) {
        return new ArrayList<>(applicationContext.getBeansWithAnnotation(Monitor.class).values());
    }
}
