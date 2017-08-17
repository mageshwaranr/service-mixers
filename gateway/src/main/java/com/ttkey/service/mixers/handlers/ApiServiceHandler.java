package com.ttkey.service.mixers.handlers;

import com.google.gson.Gson;
import com.ttkey.service.mixers.cache.GatewayCache;
import com.ttkey.service.mixers.model.ExecutorContext;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.InputSource;
import com.ttkey.service.mixers.model.manifest.RequestInfo;
import com.ttkey.service.mixers.rest.RestClient;
import com.ttkey.service.mixers.validators.InputValidator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import jdk.internal.util.xml.impl.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dkalidindi on 8/16/2017.
 */
public class ApiServiceHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        HttpString requestMethod = exchange.getRequestMethod();


        if (InputValidator.validate(exchange)) {
            exchange.startBlocking();
            HeaderValues apiKey = exchange.getRequestHeaders().get("x-api-key");
            String[] params = exchange.getRequestPath().split("/");
            Function function = (Function) GatewayCache.getCache().get(params[2] + "/" + params[3]);

            String actualAPIKey = function.getExpectedApi().getHeaders().get("auth").trim();
            String userAPIKey = apiKey.get(0).toString();
            if (!userAPIKey.equals(actualAPIKey)) {
                throw new Exception("unsuccesful Authentication");
            }

            // /api/v1/mixer/executor
            // Body ExecutorContext
            HttpRequest request = new HttpRequest();
            request.setBaseUri("http://localhost:9000/api/v1/mixer/executor");
            request.setMethod(RequestInfo.RequestMethod.POST);

            ExecutorContext context = new ExecutorContext();
            context.setApp(getApp(function));
            context.setFunction(function);
            context.setHttpRequest(request);

            List<Function.AliasAndIS> ips = function.getInputSources();
            List<InputSource>  sources = new ArrayList<>();
            for (Function.AliasAndIS ip : ips) {
                sources.add(getInputSources(function, ip.getSourceName()));
            }
            context.setInputSources(sources);
            request.setBody(context);

            RestClient client = new RestClient();
            client.invokeRestApi(request);
        }
    }

    private App getApp(Function function) {
        RestClient client = new RestClient();
        HttpRequest appRequest = new HttpRequest();
        appRequest.setPath("app/");
        appRequest.setMethod(RequestInfo.RequestMethod.GET);
        appRequest.setBaseUri("http://localhost:8080/");

        try {
            HttpResponse response = client.invokeRestApi(appRequest).get();
            Gson g = new Gson();
            App[] mcArray = g.fromJson((String) response.getBody(), App[].class);
            List<App> apps = Arrays.asList(mcArray);
            for (App app : apps) {
                if (app.getName().equals(function.getApp())) {
                    return app;
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputSource getInputSources(Function function, String name) {
        RestClient client = new RestClient();
        HttpRequest appRequest = new HttpRequest();
        appRequest.setPath("sources/" + function.getApp() + "/" + name);
        appRequest.setMethod(RequestInfo.RequestMethod.GET);
        appRequest.setBaseUri("http://localhost:8080");

        try {
            HttpResponse response = client.invokeRestApi(appRequest).get();
            Gson g = new Gson();
            InputSource mcArray = g.fromJson((String) response.getBody(), InputSource.class);
            return mcArray;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
