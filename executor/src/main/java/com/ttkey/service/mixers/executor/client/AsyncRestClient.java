package com.ttkey.service.mixers.executor.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import org.asynchttpclient.AsyncHttpClient;

/**
 * Created by sivarajm on 8/17/2017.
 */
public class AsyncRestClient implements RestClient {
  @Inject
  ObjectMapper objectMapper;

  @Inject
  AsyncHttpClient asyncHttpClient;

  public CompletableFuture<String> get(String url, Map<String, String> headers) {
    CompletableFuture<String> response = new CompletableFuture<>();
    asyncHttpClient
        .prepareGet(url)
        .setHeaders(getHeadersFromMap(headers))
        .execute()
        .toCompletableFuture()
        .thenAccept(resp -> response.complete(resp.getResponseBody()))
        .exceptionally(ex -> {
          response.completeExceptionally(ex);
          return null;
        });

    return response;
  }

  public CompletableFuture<String> post(String url, Object body, Map<String, String> headers) {
    CompletableFuture<String> response = new CompletableFuture<>();
    try {
      asyncHttpClient
          .preparePost(url)
          .setBody(objectMapper.writeValueAsBytes(body))
          .setHeaders(getHeadersFromMap(headers))
          .execute()
          .toCompletableFuture()
          .thenAccept(resp -> response.complete(resp.getResponseBody()))
          .exceptionally(ex -> {
            response.completeExceptionally(ex);
            return null;
          });
    } catch (JsonProcessingException e) {
      response.completeExceptionally(e);
    }
    return response;
  }

  private Headers getHeadersFromMap(Map<String, String> inputMap) {

    Headers headers = new Headers();
    inputMap.forEach((k,v) -> {
      headers.add(k, v);
    });
    return headers;
  }
}
