package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.url.URLBuilder;
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
 * freemarker url directive, used as "@url" in ftl, supports override context in site config in case proxy to different context
 *
 * @author neo
 */
public class URLTag extends TagSupport implements TemplateDirectiveModel {
    private final HttpServletRequest request;
    private final DeploymentSettings deploymentSettings;

    public URLTag(HttpServletRequest request, DeploymentSettings deploymentSettings) {
        this.request = request;
        this.deploymentSettings = deploymentSettings;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);

        String url = getRequiredStringParam(params, "value");
        String scheme = getStringParam(params, "scheme");

        String completeURL = constructURL(url, scheme);
        Writer output = env.getOut();
        output.write(completeURL);
    }

    private String constructURL(String url, String scheme) {
        URLBuilder builder = new URLBuilder();
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(url);

        String targetURL;
        if (scheme != null) {
            builder.setScheme(scheme);
            builder.setServerName(request.getServerName());
            builder.setDeploymentPorts(deploymentSettings.getHTTPPort(), deploymentSettings.getHTTPSPort());
            targetURL = builder.buildFullURL();
        } else {
            targetURL = builder.buildRelativeURL();
        }
        return targetURL;
    }
}
