package com.zyd.core.platform.web.site.view;

import freemarker.template.TemplateModelException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class DefaultFreemarkerViewTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    DefaultFreemarkerView defaultFreemarkerView;

    @Before
    public void createDefaultFreemarkerView() {
        defaultFreemarkerView = new DefaultFreemarkerView();
    }

    @Test
    public void assertTagNameIsAvailable() throws TemplateModelException {
        exception.expect(TemplateModelException.class);
        exception.expectMessage("reservedTag is reserved name in model as @reservedTag");

        defaultFreemarkerView.assertTagNameIsAvailable(new Object(), "reservedTag");
    }
}
