package com.ttkey.service.mixers.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;

/**
 * Created by dkalidindi on 8/16/2017.
 */
public class ServiceHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hi");
    }
}
