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
 * Created by nibunangs on 17-Aug-2017.
 */
@Entity @Getter @Setter @NoArgsConstructor @ToString
public class KeyValueEO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String key, value;

    public KeyValueEO(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
