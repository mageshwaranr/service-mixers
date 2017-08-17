package com.ttkey.service.mixers.model.manifest;

import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class Function {

    private String name, app, className, methodName, args;

    private RequestInfo expectedApi;

    private List<AliasAndIS> inputSources;

    public List<AliasAndIS> getInputSources() {
        return inputSources == null ? Collections.emptyList() : inputSources;
    }

    @Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
    public static class AliasAndIS {
        private String alias;
        private String sourceName;
    }
}
