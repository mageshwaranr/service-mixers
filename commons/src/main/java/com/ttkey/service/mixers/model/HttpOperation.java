package com.ttkey.service.mixers.model;

import static java.util.Collections.emptyMap;

import java.util.Map;

import com.ttkey.service.mixers.model.manifest.RequestInfo.RequestMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HttpOperation {

    private RequestMethod method;

    private String path;

    private Map<String,Object> queryParams = emptyMap();

    private Map<String,Integer> pathTemplates = emptyMap();

    private Map<String,String> pathParams = emptyMap();

    // this should be multi-map ?
    private Map<String,String> headers = emptyMap();

    private Object body;


}
