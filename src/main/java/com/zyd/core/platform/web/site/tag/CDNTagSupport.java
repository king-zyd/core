package com.zyd.core.platform.web.site.tag;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.cdn.CDNSettings;
import com.zyd.core.platform.web.url.URLBuilder;
import com.zyd.core.util.AssertUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author neo
 */
public class CDNTagSupport extends TagSupport {
    final HttpServletRequest request;
    final RuntimeSettings runtimeSettings;
    final CDNSettings cdnSettings;
    final DeploymentSettings deploymentSettings;

    public CDNTagSupport(HttpServletRequest request, RuntimeSettings runtimeSettings, CDNSettings cdnSettings, DeploymentSettings deploymentSettings) {
        this.request = request;
        this.runtimeSettings = runtimeSettings;
        this.cdnSettings = cdnSettings;
        this.deploymentSettings = deploymentSettings;
    }

    String constructCDNURL(String url) {
        if (url.startsWith("http://") || url.startsWith("https://"))
            return url;

        if (!supportCDN())
            return constructLocalURL(url);

        String cdnHost = determineCDNHost(url);
        URLBuilder builder = new URLBuilder();

        builder.setScheme(request.getScheme());
        builder.setServerName(cdnHost);
        builder.setDeploymentPorts(HTTPConstants.STANDARD_PORT_HTTP, HTTPConstants.STANDARD_PORT_HTTPS); // for CDN, we use standard ports
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(url);
        builder.addParam("version", runtimeSettings.getVersion());
        return builder.buildFullURL();
    }

    boolean supportCDN() {
        String[] cdnHosts = cdnSettings.getCDNHosts();
        if (cdnHosts == null || cdnHosts.length == 0)
            return false;
        if (request.isSecure() && !cdnSettings.supportHTTPS())
            return false;
        return true;
    }

    /**
     * use hash to generate deterministic spread cdn hosts
     *
     * @param url the relative url
     * @return cdn host
     */
    String determineCDNHost(String url) {
        int index = Math.abs(url.hashCode() % cdnSettings.getCDNHosts().length);
        return cdnSettings.getCDNHosts()[index];
    }

    String constructLocalURL(String url) {
        URLBuilder builder = new URLBuilder();
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(url);
        builder.addParam("version", runtimeSettings.getVersion());
        return builder.buildRelativeURL();
    }

    // TODO(neo): tuning by not using String.format, according to profiling result
    String buildMultipleResourceTags(String srcKey, String resourceDir, String tagTemplate, Map<String, Object> params) throws IOException, TemplateModelException {
        StringBuilder builder = new StringBuilder();

        String srcValue = getRequiredStringParam(params, srcKey);

        String[] srcItems = srcValue.split(",");
        for (String srcItem : srcItems) {
            String src = srcItem.trim();
            AssertUtils.assertHasText(src, "src can not be empty");
            if (!src.startsWith("/")) {
                src = String.format("%s/%s", resourceDir, src);
            }
            src = constructCDNURL(src);
            String code = String.format(tagTemplate, src, buildExtAttributes(params, srcKey));
            builder.append(code).append('\n');
        }

        return builder.toString();
    }

    String buildExtAttributes(Map<String, Object> params, String excludedKey) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String key = param.getKey();
            if (!excludedKey.equals(key)) {
                Object value = param.getValue();
                if (value instanceof SimpleScalar) {
                    builder.append(String.format(" %s=\"%s\"", key, value));
                }
            }
        }
        return builder.toString();
    }
}
