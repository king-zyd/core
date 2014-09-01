package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.cdn.CDNSettings;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * freemarker url directive, used as "@cdn" in ftl
 *
 * @author neo
 */
public class CDNTag extends CDNTagSupport implements TemplateDirectiveModel {
    public CDNTag(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings, DeploymentSettings deploymentSettings) {
        super(request, runtimeSettings, cdnSettings, deploymentSettings);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String url = getRequiredStringParam(params, "value");

        String completeURL = constructCDNURL(url);
        Writer output = env.getOut();
        output.write(completeURL);
    }
}
