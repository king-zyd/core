package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.platform.web.site.cdn.CDNSettings;
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
 * freemarker url directive, used as "@js" in ftl
 *
 * @author walter.zheng
 */
public class JSTag extends CDNTagSupport implements TemplateDirectiveModel {
    public static final String TEMPLATE_JS_TAG = "<script type=\"text/javascript\" src=\"%s\"%s></script>";

    private final SiteSettings siteSettings;

    public JSTag(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings, SiteSettings siteSettings, DeploymentSettings deploymentSettings) {
        super(request, runtimeSettings, cdnSettings, deploymentSettings);
        this.siteSettings = siteSettings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String tags = buildJSTags(params);
        Writer output = env.getOut();
        output.write(tags);
    }

    String buildJSTags(Map<String, Object> params) throws IOException, TemplateModelException {
        return buildMultipleResourceTags("src", siteSettings.getJSDir(), TEMPLATE_JS_TAG, params);
    }
}
