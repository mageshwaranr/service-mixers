package com.ttkey.service.mixers.model.manifest;

import lombok.*;

import java.util.List;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Getter @Setter @ToString
public class Function {

    private String name, app;

    private RequestInfo expectedApi;

    private List<AliasAndIS> inputSources;

    @Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
    public static class AliasAndIS {
        private String alias;
        private String isName;
    }
}
