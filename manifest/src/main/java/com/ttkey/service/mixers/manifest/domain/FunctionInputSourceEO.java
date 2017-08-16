package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.Function;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.ttkey.service.mixers.manifest.domain.InputSourceEO.createId;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class FunctionInputSourceEO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public FunctionInputSourceEO(String appName, String alias, String inputSourceName) {
        InputSourceEO inputSourceEO = new InputSourceEO();
        inputSourceEO.setId(createId.apply(inputSource.getAppName(), inputSource.getSourceName()));

        setInputSource(inputSourceEO);
        setAlias(alias);
    }

    public Function.AliasAndIS toAlaisAndIS() {
        return new Function.AliasAndIS(getAlias(), getInputSource().getSourceName());
    }

}
