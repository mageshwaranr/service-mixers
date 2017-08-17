package com.ttkey.service.mixers.executor.resources;

import com.google.gson.Gson;
import com.ttkey.service.mixers.executor.loader.FunctionContextFactory;
import com.ttkey.service.mixers.executor.model.FunctionContext;
import com.ttkey.service.mixers.executor.util.TypeConverter;
import com.ttkey.service.mixers.model.ExecutorContext;
import com.ttkey.service.mixers.model.HttpResponse;
import com.ttkey.service.mixers.model.manifest.InputSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttkey.service.mixers.executor.loader.FunctionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/v1/mixer/executor")
public class FunctionExecutorService {

    private static final Logger log = LoggerFactory.getLogger(FunctionExecutorService.class);

    @Inject
    private FunctionLoader functionLoader;

    @Inject
    private FunctionContextFactory functionContextFactory;

    @Inject
    private Gson gson;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse execute(ExecutorContext executorContext) {

        ClassLoader classLoader = functionLoader.findClassLoader(executorContext.getFunction().getApp(), executorContext.getFunction().getName());
        FunctionContext functionContext = functionContextFactory.newFunctionContext(executorContext.getApp(), executorContext.getFunction(), executorContext.getInputSources(), executorContext.getHttpRequest());
        HttpResponse httpResponse = new HttpResponse();
        try {
            log.info("App: {}, Function: {}, Class: {}, Method: {}, classLoader: {}", executorContext.getFunction().getApp(), executorContext.getFunction().getName(), executorContext.getFunction().getClassName(), executorContext.getFunction().getMethodName(), classLoader);
            Class classToLoad = Class.forName(executorContext.getFunction().getClassName(), true, classLoader);

            for (Method m : classToLoad.getMethods()) {
                if (executorContext.getFunction().getMethodName().equals(m.getName())) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == functionContext.getArgs().length) {
                        Object methodParams[] = new Object[params.length];
                        for(int i = 0; i< params.length; i++) {
                            if(params[i].isPrimitive()) {
                                methodParams[i]= TypeConverter.convertToPrimitiveOrEnum(functionContext.getArgs()[i].toString(), params[i]);
                            } else {
                                methodParams[i]= gson.fromJson(functionContext.getArgs()[i].toString(), params[i]);
                            }
                        }
                        Object instance = classToLoad.newInstance();
                        Object responseObject = m.invoke(instance, methodParams);
                        httpResponse.setStatusCode(200);
                        httpResponse.setBody(gson.toJson(responseObject));
                        return httpResponse;
                    } else {
                        httpResponse.setStatusCode(500);
                        httpResponse.setBody("Method params didn't match function context arguments");
                        return httpResponse;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Function executor failed {}", e);
            httpResponse.setStatusCode(500);
            httpResponse.setBody(e.getMessage());
            return httpResponse;
        }

        return httpResponse;
    }

    private Object getRequestObject(String requestClassName, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        Class requestClass = Class.forName(requestClassName, true, classLoader);
        ObjectMapper objectMapper = new ObjectMapper();
        Object requestObject = objectMapper.readValue("{\"n\": 10}", requestClass);
        return requestObject;
    }
}
