package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.platform.web.site.cdn.CDNSettings;
import com.zyd.core.platform.web.url.URLBuilder;
import com.zyd.core.util.StringUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * User: gary.zeng
 * Date: 14-6-10
 */
public class SeaJsUrlTag extends CDNTagSupport implements TemplateDirectiveModel {

    private static final String TEMPLATE_SINGLE = "\"%s\"";

    private final SiteSettings siteSettings;

    public SeaJsUrlTag(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings, SiteSettings siteSettings, DeploymentSettings deploymentSettings) {
        super(request, runtimeSettings, cdnSettings, deploymentSettings);
        this.siteSettings = siteSettings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String tags = buildContent(params);
        Writer output = env.getOut();
        output.write(tags);
    }

    String buildContent(Map<String, Object> params) throws IOException, TemplateModelException {
        String urlValue = getRequiredStringParam(params, "value");
        String[] srcItems = urlValue.split(",");
        if (srcItems.length == 1) {
            // single url
            return buildSingleSeaJsUrl(srcItems[0]);
        }
        return buildMultipleSeaJsUrl(srcItems);
    }

    private String processUrl(String oriUrl) {
        if (runtimeSettings.getEnvironment() != RuntimeEnvironment.PROD) {
            String jsDir = siteSettings.getJSDir();
            String url = oriUrl.trim();
            if (!StringUtils.hasText(jsDir)) {
                throw new RuntimeException("No jsDir configuration found!");
            }
            if (!jsDir.endsWith("/")) {
                jsDir += '/';
            }
            if (oriUrl.startsWith("/")) {
                url = oriUrl.substring(1);
            }
            return jsDir + url;
        }
        return oriUrl.trim();
    }

    private String buildSingleSeaJsUrl(String src) {
        String url = processUrl(src);
        URLBuilder builder = new URLBuilder();
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(url);
        return String.format(TEMPLATE_SINGLE, builder.buildRelativeURL());
    }

    private String buildMultipleSeaJsUrl(String[] srcItems) {
        StringBuilder builder = new StringBuilder("[");
        for (String src : srcItems) {
            String url = processUrl(src);
            builder.append(String.format(TEMPLATE_SINGLE, url)).append(',');
        }
        return builder.toString().substring(0, builder.lastIndexOf(",")) + ']';
    }
}
