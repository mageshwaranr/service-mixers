package com.ttkey.service.mixers.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HttpOperation {

    private String path;

    private Map<String,Object> queryParams;

    private Map<String,Integer> pathTemplates;

    private Map<String,String> pathParams;

    private Object body;


}
