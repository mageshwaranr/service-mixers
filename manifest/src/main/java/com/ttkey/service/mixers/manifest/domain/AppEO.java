package com.ttkey.service.mixers.manifest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by nibunangs on 14-Aug-2017.
 */
@Entity
@Getter@Setter
public class AppEO {

    @Id
    private String name;

    private String configsJSON;

}
