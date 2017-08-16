package com.ttkey.service.mixers.manifest.service.exception;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
