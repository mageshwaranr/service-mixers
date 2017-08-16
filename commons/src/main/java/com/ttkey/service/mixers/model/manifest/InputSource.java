package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class InputSource {

    private String appName, sourceName;

    private SourceType sourceType;

    private RequestInfo request;

    private Map<String, String> headers;

    public enum SourceType {
        REST
    }

}