package com.ttkey.service.mixers.model;

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

    private Map<String,Object> queryParams;

    private Map<String,Integer> pathTemplates;

    private Map<String,String> pathParams;

    private Object body;


}
