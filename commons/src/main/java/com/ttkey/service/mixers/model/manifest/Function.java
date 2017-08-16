package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter@Setter@ToString
public class Function {

    private String name, app;

    private RequestInfo request;

    private List<AliasAndIS> inputSources;

    @Getter@Setter@ToString
    public static class AliasAndIS {
        private String alias;
        private String isName;
    }
}
