package com.ttkey.service.mixers.validators;

import com.ttkey.service.mixers.exception.GatewayException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;

/**
 * Created by dkalidindi on 8/17/2017.
 */
public class InputValidator {

    public static boolean validate(HttpServerExchange exchange) {

        boolean success = true;

        HeaderValues apiKey = exchange.getRequestHeaders().get("x-api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            success = false;
        }
        return success;
    }
}
