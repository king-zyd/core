package com.zyd.core.platform.web.rest.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raymond
 */
public class APIUriUtils {

    public static String buildUri(String interfaceUri, String action) {
        if (interfaceUri.endsWith("/")) {
            if (action.startsWith("/"))
                return StringUtils.removeEnd(interfaceUri, "/") + action;
            else
                return interfaceUri + action;
        } else {
            if (action.startsWith("/"))
                return interfaceUri + action;
            else
                return interfaceUri + "/" + action;
        }
    }

    public static String formatPathVariable(String action, Annotation[][] annotations, Object[] args) {
        int index = 0;
        List<String> searchList = new ArrayList<>();
        List<String> replacementList = new ArrayList<>();
        for (Annotation[] annotation : annotations) {
            for (Annotation item : annotation) {
                if (item.annotationType().equals(PathVariable.class)) {
                    PathVariable pathVariable = (PathVariable) item;
                    searchList.add("{" + pathVariable.value() + "}");
                    replacementList.add(args[index].toString());
                }
            }
            index++;
        }
        return StringUtils.replaceEach(action, searchList.toArray(new String[searchList.size()]), replacementList.toArray(new String[replacementList.size()]));
    }

}
