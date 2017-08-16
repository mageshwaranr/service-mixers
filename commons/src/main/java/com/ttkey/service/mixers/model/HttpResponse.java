package com.ttkey.service.mixers.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class HttpResponse extends HttpOperation {

    private int statusCode;

    public Object getResponseBody(){
        return super.getBody();
    }

}
