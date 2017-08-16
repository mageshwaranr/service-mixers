package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.Function;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter
public class FunctionEO {

    public static final BiFunction<String, String, String> createId = (app, funktionName) -> app + "-" + funktionName;

    @Id
    private String id;

    @Column(nullable = false)
    private String name, app;

    private byte[] executable;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "REQUEST_ID", unique = true, nullable = false)
    private RequestEO expectedAPI;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "funktion_id")
    private List<FunctionInputSourceEO> inputSources;

    public void copy(Function functionVO) {
        setName(functionVO.getName());
        setApp(functionVO.getApp());

        if (getExpectedAPI() == null)
            setExpectedAPI(new RequestEO());
        getExpectedAPI().copy(functionVO.getExpectedApi());

        getInputSources().clear();
        functionVO.getInputSources().forEach(
                aliasAndIS -> getInputSources().add(new FunctionInputSourceEO(getApp(), aliasAndIS.getAlias(), aliasAndIS.getIsName())));
    }

    public Function toFunction() {
        Function function = new Function();
        function.setApp(getApp());
        function.setName(getName());

        function.setExpectedApi(getExpectedAPI().toRequestInfo());

        List<Function.AliasAndIS> inputSources = getInputSources().stream().map(FunctionInputSourceEO::toAlaisAndIS)
                .collect(Collectors.toList());
        function.setInputSources(inputSources);

        return function;
    }
}
