package com.zyd.core.platform.monitor.web;

import com.zyd.core.platform.monitor.Track;
import com.zyd.core.platform.monitor.performance.PerformanceStatistics;
import com.zyd.core.platform.web.ControllerHelper;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.request.RequestContextInterceptor;
import com.zyd.core.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author neo
 */
public class TrackInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(TrackInterceptor.class);
    private RequestContext requestContext;
    private PerformanceStatistics performanceStatistics;
    private RequestContextInterceptor requestContextInterceptor;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AssertUtils.assertTrue(requestContextInterceptor.initialized(request), "trackInterceptor depends on requestContextInterceptor, please check WebConfig");

        Track track = ControllerHelper.findMethodOrClassLevelAnnotation(handler, Track.class);
        if (track != null) {
            trackProcess(track);
        }
    }

    private void trackProcess(Track track) {
        Date startTime = requestContext.getRequestDate();

        long elapsedTime = System.currentTimeMillis() - startTime.getTime();
        if (warningEnabled(track) && elapsedTime > track.warningThresholdInMs()) {
            logger.warn("process took longer than track threshold, elapsedTime(ms)={}", elapsedTime);
        }

        performanceStatistics.record(requestContext.getAction(), elapsedTime, startTime);
    }

    private boolean warningEnabled(Track track) {
        return track.warningThresholdInMs() > 0;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setPerformanceStatistics(PerformanceStatistics performanceStatistics) {
        this.performanceStatistics = performanceStatistics;
    }

    @Inject
    public void setRequestContextInterceptor(RequestContextInterceptor requestContextInterceptor) {
        this.requestContextInterceptor = requestContextInterceptor;
    }
}
