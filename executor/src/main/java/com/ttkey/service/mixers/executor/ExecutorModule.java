package com.ttkey.service.mixers.executor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ttkey.service.mixers.executor.client.AsyncRestClient;
import com.ttkey.service.mixers.executor.client.RestClient;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig.Builder;

/**
 * Created by sivarajm on 8/17/2017.
 */
public class ExecutorModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(RestClient.class).to(AsyncRestClient.class);
    }

    @Provides
    public ObjectMapper objectMapperProvider() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    @Singleton
    public AsyncHttpClient asyncHttpClientProvider() {
        final Builder builder = new Builder();
        builder.setMaxConnections(100);
        builder.setMaxConnectionsPerHost(25);
        return new DefaultAsyncHttpClient(builder.build());
    }

    @Provides
    @Singleton
    public com.ttkey.service.mixers.rest.RestClient restClient(){
        return new com.ttkey.service.mixers.rest.RestClient();
    }

    @Provides
    @Singleton
    public Gson newGson(){
        return new GsonBuilder().setLenient().create();
    }
}
