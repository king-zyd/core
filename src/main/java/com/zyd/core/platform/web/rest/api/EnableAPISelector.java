package com.zyd.core.platform.web.rest.api;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author Raymond
 */
public class EnableAPISelector extends AdviceModeImportSelector<EnableAPI> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        if (adviceMode != AdviceMode.PROXY)
            throw new IllegalStateException("@EnableAPI only support PROXY advice mode.");
        return new String[] { EnableAPIConfiguration.class.getName() };
    }
}
