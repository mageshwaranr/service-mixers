package com.ttkey.service.mixers.executor.template;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import com.google.gson.Gson;
import com.ttkey.service.mixers.executor.model.FunctionContext;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import org.junit.Test;

public class TemplateEngineTest {

    private TemplateEngine engine = new TemplateEngine();
    private Gson gson = new Gson();


    @Test
    public void replaceTemplates() throws Exception {

        FunctionContext context = new FunctionContext();
        HttpRequest request = sampleHttpRequest();
        request.setQueryParams(singletonMap("queryParam", "value"));
        context.setRequest(request);

        // mimic an REST source
        HttpResponse zonesSource = new HttpResponse();
        // mimic an REST API returning region to availability zone mapping.
        String body = "{ \"us-west\" : [ \"us-west-1\",\"us-west-2\",\"us-west-3\"], \"us-east\" : [ \"us-east-1\",\"us-east-2\",\"us-east-3\"]  }";
        zonesSource.setBody(gson.fromJson(body, Map.class));
        context.setSources(singletonMap("zones", zonesSource));

        //function args depending on a source and expectedAPI params
        String[] functionArgs = {"@{sources['zones'].body['us-west']}", " @{expectedAPI.queryParams['queryParam']}"};

        Object[] replacedTemplates = engine.replaceTemplates(functionArgs, context);
        assertEquals(asList("us-west-1", "us-west-2", "us-west-3"), replacedTemplates[0]);

        assertEquals("value", replacedTemplates[1].toString().trim());
    }

    private HttpRequest sampleHttpRequest() {
        HttpRequest request = new HttpRequest();
        request.setBaseUri("http://localhost:8080");
        request.setPath("/input/function/functionA");
        request.setPathTemplates(singletonMap("functionA", 2));
        return request;
    }
}