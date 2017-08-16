package com.ttkey.service.mixers.manifest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter @ToString @NoArgsConstructor
public class HeadersEO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String key, value;

    public HeadersEO(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
