package com.zyd.core.platform.web.site.view;

import freemarker.cache.TemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @author neo
 */
public class DefaultFreeMarkerConfigurer extends FreeMarkerConfigurer {
    // replace default template loader with html escaping implementation, to prevent XSS by default.
    @Override
    protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
        TemplateLoader loader = super.getTemplateLoaderForPath(templateLoaderPath);
        return new HTMLEscapeTemplateLoader(loader);
    }
}
