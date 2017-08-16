package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.InputSource;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.function.BiFunction;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter
public class InputSourceEO {
    @Id
    private String id;

    private String appName, sourceName;

    private InputSource.SourceType sourceType;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "REQUEST_ID", unique = true, nullable = false)
    private RequestEO request;

    public static BiFunction<String, String, String> createId = (appName, inputSourceName) -> appName + "-" + inputSourceName;

    public void copy(InputSource inputSource) {
        setAppName(inputSource.getAppName());
        setSourceName(inputSource.getSourceName());
        setSourceType(inputSource.getSourceType());

        if (getRequest() == null)
            setRequest(new RequestEO());
        getRequest().copy(inputSource.getRequest());
    }

    public InputSource toInputSource() {
        InputSource inputSource = new InputSource();
        inputSource.setAppName(getAppName());
        inputSource.setSourceName(getSourceName());
        inputSource.setSourceType(getSourceType());
        inputSource.setRequest(getRequest().toRequestInfo());

        return inputSource;
    }

}
