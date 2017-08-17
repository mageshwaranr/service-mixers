package com.ttkey.service.mixers.executor.loader;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttkey.service.mixers.executor.model.FunctionContext;
import com.ttkey.service.mixers.executor.template.TemplateEngine;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.Function.AliasAndIS;
import com.ttkey.service.mixers.model.manifest.InputSource;
import com.ttkey.service.mixers.model.manifest.RequestInfo;
import com.ttkey.service.mixers.rest.RestClient;

public class FunctionContextFactory {

    private TemplateEngine templateEngine;
    private Gson gson;
    private RestClient restClient;
    private Type mapType = new TypeToken<Map<String,String>>(){}.getType();

    @Inject
    public FunctionContextFactory(TemplateEngine templateEngine, Gson gson, RestClient client){
        this.gson = gson;
        this.templateEngine = templateEngine;
        this.restClient = client;
    }

    public FunctionContext newFunctionContext(App app, Function fn, List<InputSource> sources, HttpRequest request) {
        List<AliasAndIS> inputSources = fn.getInputSources();
        FunctionContext context = new FunctionContext();
        context.setRequest(request);
        context.setAppConfigs(app.getConfigs());
        enrichSourceInfo(sources, inputSources, context);
        context.setArgs(templateEngine.replaceTemplates(fn.getArgs(),context));
        return context;
    }

    private void enrichSourceInfo(List<InputSource> sources, List<AliasAndIS> inputSources, FunctionContext context) {
        Map<String,InputSource> sourcesByName = new HashMap<>();
        sources.forEach(inputSource -> sourcesByName.put(inputSource.getSourceName(), inputSource));

        Map<String,HttpRequest> srcRequests = new HashMap<>();
        inputSources.forEach(aliasAndIS -> srcRequests.put(aliasAndIS.getAlias(), prepareRequest(context,sourcesByName.get(aliasAndIS.getSourceName()))));

        Map<String, HttpResponse> httpResponses = this.restClient.invokeRestApisSync(srcRequests);
        context.setSources(httpResponses);
    }

    private HttpRequest prepareRequest(FunctionContext context,InputSource src){
        HttpRequest request = new HttpRequest();
        RequestInfo srcRequest = src.getRequest();
        if(srcRequest.getBody() != null){
            request.setBody(templateEngine.replaceTemplate(srcRequest.getBody(),context));
        }
        request.setPath(templateEngine.replaceTemplate(srcRequest.getUri(),context).toString());
        if(context.getRequest().getHeaders().size() > 0) {
            String headerJson = templateEngine.replaceTemplate(gson.toJson(context.getRequest().getHeaders()), context).toString();
            request.setHeaders(gson.fromJson(headerJson, mapType));
        }
        request.setMethod(srcRequest.getHttpVerb());
        return request;
    }

}
