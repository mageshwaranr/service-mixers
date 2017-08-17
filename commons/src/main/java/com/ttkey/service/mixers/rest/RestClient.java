package com.ttkey.service.mixers.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import co.paralleluniverse.fibers.httpasyncclient.FiberCloseableHttpAsyncClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

    private static int CONCURRENCY_LEVEL = 25_000;

    private Gson gson;

    private CloseableHttpAsyncClient client;

    private Logger log = LoggerFactory.getLogger(getClass());

    public RestClient() {
        this(CONCURRENCY_LEVEL);
    }

    public RestClient(int concurrency) {
        client = FiberCloseableHttpAsyncClient.wrap(HttpAsyncClients.custom().
                setMaxConnPerRoute(concurrency).
                setMaxConnTotal(concurrency).
                build());
        client.start();
        gson = new GsonBuilder().setLenient().create();
    }


    public Map<String, HttpResponse> invokeRestApisSync(Map<String, HttpRequest> reqDetails) {
        Map<String, CompletableFuture<HttpResponse>> asyncResponses = invokeRestApis(reqDetails);
        CompletableFuture.allOf(asyncResponses.values().toArray(new CompletableFuture[0])).join();
        Map<String, HttpResponse> responses = new HashMap<>();
        for (Entry<String, CompletableFuture<HttpResponse>> entry : asyncResponses.entrySet()) {
            responses.put(entry.getKey(), entry.getValue().join());
        }
        return responses;
    }

    public Map<String, CompletableFuture<HttpResponse>> invokeRestApis(Map<String, HttpRequest> reqDetails) {
        Map<String, CompletableFuture<HttpResponse>> asyncResponses = new HashMap<>();
        for (Entry<String, HttpRequest> entry : reqDetails.entrySet()) {
            asyncResponses.put(entry.getKey(), invokeRestApi(entry.getValue()));
        }
        return asyncResponses;
    }

    public CompletableFuture<HttpResponse> invokeRestApi(HttpRequest reqDetails) {
        CompletableFuture<HttpResponse> response = new CompletableFuture<>();
        HttpUriRequest request = toRequest(reqDetails);
        client.execute(request, new FutureCallback<org.apache.http.HttpResponse>() {
            @Override
            public void completed(org.apache.http.HttpResponse result) {
                toResponse(result, reqDetails, response);
            }

            @Override
            public void failed(Exception ex) {
                log.error("Unable to execute the HttpAPi for " + request.getMethod() + ":" + request.getURI(), ex);
                response.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                log.warn("Cancelling API execution of {} ; {}", request.getMethod(), request.getURI());
                response.cancel(false);
            }
        });
        return response;
    }

    private void toResponse(org.apache.http.HttpResponse result, HttpRequest req, CompletableFuture<HttpResponse> future) {
        try {
            HttpResponse response = new HttpResponse();
            Map<String, String> headers = new HashMap<>();
            for (Header header : result.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }
            response.setHeaders(headers);
            response.setStatusCode(result.getStatusLine().getStatusCode());
            String entity = EntityUtils.toString(result.getEntity());
            response.setBody(entity);
            response.setMethod(req.getMethod());
            response.setPath(req.getPath());
            future.complete(response);
        } catch (Exception e) {
            log.error("Unable to parse HttpResponse of " + req, e);
            future.completeExceptionally(e);
        }
    }

    private HttpUriRequest toRequest(HttpRequest inReq) {
        HttpUriRequest request;
        String uri = buildUri(inReq);
        switch (inReq.getMethod()) {
            case GET:
                request = new HttpGet(uri);
                break;
            case PUT:
                request = enrichWithBody(new HttpPut(uri), inReq);
                break;
            case POST:
                request = enrichWithBody(new HttpPost(uri), inReq);
                break;
            case PATCH:
                request = enrichWithBody(new HttpPatch(uri), inReq);
                break;
            case DELETE:
                request = new HttpDelete(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported HTTP method " + inReq.getMethod());
        }
        inReq.getHeaders().forEach(request::addHeader);

        return request;
    }

    private String buildUri(HttpRequest req) {
        StringBuilder sb = new StringBuilder();
        if (req.getBaseUri() != null) {
            sb.append(req.getBaseUri()).append("/");
        }
        sb.append(req.getPath());
        boolean isFirst = true;
        for (Entry<String, Object> entry : req.getQueryParams().entrySet()) {
            if (isFirst) {
                isFirst = false;
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    private HttpEntityEnclosingRequestBase enrichWithBody(HttpEntityEnclosingRequestBase req, HttpRequest inReq) {
        Object body = inReq.getBody();
        if (body instanceof String) {
            req.setEntity(new StringEntity((String) body, ContentType.APPLICATION_JSON));
        } else {
            req.setEntity(new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON));
        }
        return req;
    }
}
