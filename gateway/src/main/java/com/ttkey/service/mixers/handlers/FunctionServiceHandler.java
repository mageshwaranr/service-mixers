package com.ttkey.service.mixers.handlers;

import com.google.gson.Gson;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.RequestInfo;
import com.ttkey.service.mixers.rest.RestClient;
import com.ttkey.service.mixers.validators.InputValidator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import org.apache.commons.io.IOUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dkalidindi on 8/16/2017.
 */
public class FunctionServiceHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        HttpString requestMethod = exchange.getRequestMethod();

        switch (RequestInfo.RequestMethod.valueOf(requestMethod.toString())) {

            case POST: {
                exchange.startBlocking();
                Gson g = new Gson();
                Function func = g.fromJson(IOUtils.toString(exchange.getInputStream()), Function.class);

                HttpRequest request = new HttpRequest();
                request.setBaseUri("http://localhost:8080");
                request.setMethod(RequestInfo.RequestMethod.POST);

                Map map = new HashMap();
                map.put("Content-Type", "application/json");
                request.setHeaders(map);
                request.setBody(func);

                RestClient client = new RestClient();
                client.invokeRestApi(request);
                break;
            }
            case GET: {
                if (InputValidator.validate(exchange)) {
                    HeaderValues apiKey = exchange.getRequestHeaders().get("x-api-key");
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported HTTP method " + requestMethod.toString());
        }
    }
}
