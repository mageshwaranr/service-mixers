package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class RequestInfo {

    private RequestMethod httpVerb;

    private String uri, body;

    public enum RequestMethod {
        GET, POST, PUT, PATCH, DELETE
    }
}
