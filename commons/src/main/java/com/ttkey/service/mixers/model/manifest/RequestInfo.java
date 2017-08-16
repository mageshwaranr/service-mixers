package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class RequestInfo {

    private RequestMethod httpVerb;

    private String uri, body;

    private Map<String, String> headers;

    public enum RequestMethod {
        GET, POST, PUT, PATCH, DELETE
    }

    public Map<String, String> getHeaders() {
        return headers == null ? Collections.emptyMap() : headers;
    }
}
