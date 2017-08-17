package com.ttkey.service.mixers.exception;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
public class GatewayException extends Exception{

    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
