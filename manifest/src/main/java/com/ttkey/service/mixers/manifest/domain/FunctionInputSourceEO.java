package com.ttkey.service.mixers.manifest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter@Setter
public class FunctionInputSourceEO {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String alias;

    @OneToOne(optional = false)
    @JoinColumn(name = "InputSource_ID", nullable = false)
    private InputSourceEO inputSource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public InputSourceEO getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceEO inputSource) {
        inputSource.setId(InputSourceEO.createId.apply(inputSource.getAppName(), inputSource.getSourceName()));
        this.inputSource = inputSource;
    }

}
