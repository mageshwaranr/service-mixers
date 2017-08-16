package com.ttkey.service.mixers.executor.model;


import java.util.Map;

import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FunctionContext {

    private Map<String,HttpResponse> sources;
    private HttpRequest request;
    private Object[] args;

}
