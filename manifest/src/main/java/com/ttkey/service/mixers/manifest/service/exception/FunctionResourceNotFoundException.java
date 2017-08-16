package com.ttkey.service.mixers.manifest.service.exception;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
public class FunctionResourceNotFoundException extends StorageException {

    public FunctionResourceNotFoundException(String message) {
        super(message);
    }

    public FunctionResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
