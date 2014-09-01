package com.zyd.core.platform.web.site.handler;

import com.zyd.core.log.ActionLoggerImpl;
import com.zyd.core.log.ActionResult;
import com.zyd.core.platform.web.site.SiteSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author neo
 */
public class ResourceNotFoundHandlerMapping extends DefaultHandlerMapping {
    private final Logger logger = LoggerFactory.getLogger(ResourceNotFoundHandlerMapping.class);

    private SiteSettings siteSettings;
    private ActionLoggerImpl actionLogger;

    public ResourceNotFoundHandlerMapping() {
        setOrder(LOWEST_PRECEDENCE);
    }

    @Override
    protected String getResultView(HttpServletRequest request) {
        String notFoundPage = siteSettings.getResourceNotFoundPage();
        actionLogger.currentActionLog().setResult(ActionResult.WARNING);
        logger.debug("return 404 page, page={}", notFoundPage);
        return notFoundPage;
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setActionLogger(ActionLoggerImpl actionLogger) {
        this.actionLogger = actionLogger;
    }
}
