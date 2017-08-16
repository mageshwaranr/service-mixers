package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class InputSource {

    private String appName, sourceName;

    private SourceType sourceType;

    private RequestInfo request;

    public enum SourceType {
        REST
    }

}
