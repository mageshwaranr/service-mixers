package com.ttkey.service.mixers.executor.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sivarajm on 8/17/2017.
 */
public interface RestClient {

  public CompletableFuture<String> get(String url, Map<String, String> headers);

  public CompletableFuture<String> post(String url, Object body, Map<String, String> headers);
}
