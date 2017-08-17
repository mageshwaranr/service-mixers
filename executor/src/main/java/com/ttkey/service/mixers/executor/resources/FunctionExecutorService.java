package com.ttkey.service.mixers.executor.resources;

import com.ttkey.service.mixers.model.ExecutorContext;
import com.ttkey.service.mixers.model.HttpResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Object execute(ExecutorContext executorContext) {

        ClassLoader classLoader = functionLoader.findClassLoader(executorContext.getFunction().getApp(), executorContext.getFunction().getName());

        //TODO: this should come from args
        String requestClassName = null;
        try {
            log.info("App: {}, Function: {}, Class: {}, Method: {}, classLoader: {}", executorContext.getFunction().getApp(), executorContext.getFunction().getName(), executorContext.getFunction().getClassName(), requestClassName, executorContext.getFunction().getMethodName(), classLoader);
            Class classToLoad = Class.forName(executorContext.getFunction().getClassName(), true, classLoader);
            Object requestObject = getRequestObject(requestClassName, classLoader);
            Method method = classToLoad.getDeclaredMethod(executorContext.getFunction().getMethodName(), requestObject.getClass());
            Object instance = classToLoad.newInstance();
            return method.invoke(instance, requestObject);
        } catch (ClassNotFoundException e) {
            log.error("Unable to load class", e);
            throw new WebApplicationException("is there a class named " + executorContext.getFunction().getClassName(), e);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("Is there a method named " + executorContext.getFunction().getMethodName() + " in class " + executorContext.getFunction().getClassName(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("IllegalAccess Exception : " + e.getMessage(),e);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("Instantiation Exception : " + e.getMessage(),e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("Invocation Exception : " + e.getMessage(),e);
        } catch (JsonParseException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("Json Parse Exception : " + e.getMessage(),e);
        } catch (JsonMappingException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("Json Mapping Exception : " + e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException("IO Exception : " + e.getMessage(),e);
        }
    }

    private Object getRequestObject(String requestClassName, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        Class requestClass = Class.forName(requestClassName, true, classLoader);
        ObjectMapper objectMapper = new ObjectMapper();
        Object requestObject = objectMapper.readValue("{\"n\": 10}", requestClass);
        return requestObject;
    }
}
