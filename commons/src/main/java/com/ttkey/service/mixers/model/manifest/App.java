package com.ttkey.service.mixers.model.manifest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@ToString @Getter @Setter
public class App {

    private String name;

    private Map<String, String> configs;

}
