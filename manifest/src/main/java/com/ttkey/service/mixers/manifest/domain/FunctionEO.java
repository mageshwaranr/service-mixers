package com.ttkey.service.mixers.manifest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter@Setter
public class FunctionEO {

    public static final BiFunction<String, String, String> createId = (app, funktionName) -> app + "-" + funktionName;

    @Id
    private String id;

    @Column(nullable = false)
    private String name, app;

    private byte[] executable;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "REQUEST_ID", unique = true, nullable = false)
    private RequestEO request;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "funktion_id")
    private List<FunctionInputSourceEO> inputSources;

    public void copy(FunctionEO functionVO) {
        getInputSources().clear();
        getInputSources().addAll(functionVO.getInputSources());

        if (getRequest() != null)
            getRequest().copy(functionVO.getRequest());
        else
            setRequest(functionVO.getRequest());
    }
}
