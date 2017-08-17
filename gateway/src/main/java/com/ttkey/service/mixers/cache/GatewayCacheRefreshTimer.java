package com.ttkey.service.mixers.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.RequestInfo;
import com.ttkey.service.mixers.rest.RestClient;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GatewayCacheRefreshTimer extends TimerTask {
    public void run() {

        RestClient client = new RestClient();
        HttpRequest request = new HttpRequest();
        request.setPath("function/");
        request.setMethod(RequestInfo.RequestMethod.GET);
        request.setBaseUri("http://localhost:8080");

        try {
            HttpResponse response = client.invokeRestApi(request).get();
            Gson g = new Gson();
            Function[] mcArray = g.fromJson((String)response.getBody(), Function[].class);
            List<Function> functions = Arrays.asList(mcArray);;
            for (Function func : functions) {
                String functionName = func.getName();
                String appName = func.getApp();
                GatewayCache.getCache().put(appName + func.getExpectedApi().getUri(), func);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
