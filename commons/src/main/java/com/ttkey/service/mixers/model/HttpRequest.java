package com.ttkey.service.mixers.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HttpRequest extends HttpOperation {

    private String baseUri;
}
