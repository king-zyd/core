package com.zyd.core.platform.web.site.tag;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

/**
 * @author neo
 */
public class MasterTag extends TagSupport implements TemplateDirectiveModel {

    public static final String IS_IGNORE_MASTER = "_isIgnoreMaster_";

    private final MasterTemplateLoader templateLoader;
    private final Map<String, Object> model;

    public MasterTag(Map<String, Object> model, MasterTemplateLoader templateLoader) {
        this.model = model;
        this.templateLoader = templateLoader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

        assertHasBody(body);

        String isIgnoreMaster = model.get(IS_IGNORE_MASTER) != null ? model.get(IS_IGNORE_MASTER).toString() : "";

        if ("Y".equalsIgnoreCase(isIgnoreMaster)) {
            body.render(env.getOut());
        } else {
            String template;
            template = getRequiredStringParam(params, "template");

            model.putAll(params);

            model.put(BodyTag.BODY_TEMPLATE, body);

            Template masterTemplate = templateLoader.loadTemplate(template);
            masterTemplate.process(model, env.getOut());
        }
    }
}
