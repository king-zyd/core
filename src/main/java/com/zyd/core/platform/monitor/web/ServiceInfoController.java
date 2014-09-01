package com.zyd.core.platform.monitor.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zyd.core.json.JSONBinder;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.exception.InternalServiceException;
import com.zyd.core.platform.monitor.web.view.ObjectDescription;
import com.zyd.core.platform.monitor.web.view.ServiceDescription;
import com.zyd.core.platform.monitor.web.view.ServiceDescriptions;
import com.zyd.core.platform.web.rest.RESTController;
import com.zyd.core.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chi
 */
@Controller
public class ServiceInfoController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(ServiceInfoController.class);
    private SpringObjectFactory springObjectFactory;

    @RequestMapping(value = "/monitor/services", method = RequestMethod.GET)
    @ResponseBody
    public ServiceDescriptions services() {
        ServiceDescriptions descriptions = new ServiceDescriptions();
        List<RESTController> controllers = springObjectFactory.getBeans(RESTController.class);
        for (RESTController controller : controllers) {
            List<ServiceDescription> serviceDescriptions = inspect(controller);
            descriptions.getDescriptions().addAll(serviceDescriptions);
        }
        return descriptions;
    }

    private List<ServiceDescription> inspect(RESTController controller) {
        try {
            return inspect(controller.getClass());
        } catch (Exception e) {
            logger.error("Failed to parse service info for controller {}", controller.getClass(), e);
            throw new InternalServiceException("Failed to get service description", e);
        }
    }

    List<ServiceDescription> inspect(Class<? extends RESTController> controllerClass) throws JsonMappingException {
        List<ServiceDescription> descriptions = new ArrayList<>();
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (isServiceMethod(method)) {
                logger.debug("Inspecting method {}.{}", controllerClass, method.getName());
                ServiceDescription description = new ServiceDescription();
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String[] paths = requestMapping.value();
                AssertUtils.assertTrue(paths.length == 1, "api service should have single path");
                description.setPath(paths[0]);
                description.setName(paths[0]);

                RequestMethod[] requestMethods = requestMapping.method();
                AssertUtils.assertTrue(requestMethods.length == 1, "api service should have single request method");
                description.setMethod(requestMethods[0].name());

                inspectPathVariablesAndRequestBody(description, method);
                description.setResponse(createObjectDescription(method.getReturnType()));

                descriptions.add(description);
            }
        }
        return descriptions;
    }

    private void inspectPathVariablesAndRequestBody(ServiceDescription description, Method method) throws JsonMappingException {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0, parameterAnnotationsLength = parameterAnnotations.length; i < parameterAnnotationsLength; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (RequestBody.class.equals(annotationType)) {
                    ObjectDescription requestDescription = createObjectDescription(method.getParameterTypes()[i]);
                    description.setRequest(requestDescription);
                } else if (PathVariable.class.equals(annotationType)) {
                    ObjectDescription pathVariableDescription = createObjectDescription(method.getParameterTypes()[i]);
                    pathVariableDescription.setName(((PathVariable) annotation).value());
                    description.addPathVariable(pathVariableDescription);
                }
            }
        }
    }

    private ObjectDescription createObjectDescription(Class<?> type) throws JsonMappingException {
        if (type == void.class || type == Void.class) {
            return null;
        }
        ObjectDescription description = new ObjectDescription();
        description.setJsonSchema(JSONBinder.getObjectMapper().generateJsonSchema(type).getSchemaNode().toString());
        return description;
    }

    //TODO(Chi): check @XmlRootElement on return value?
    private boolean isServiceMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class) && method.isAnnotationPresent(ResponseBody.class) && method.getReturnType() != Object.class;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }
}
