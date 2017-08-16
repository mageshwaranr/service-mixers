package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.RequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static com.ttkey.service.mixers.model.manifest.RequestInfo.RequestMethod;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter
public class RequestEO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private RequestMethod httpVerb;

    private String uri, body;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    @OrderColumn
    private List<HeadersEO> headers;

    public void addHeader(HeadersEO headerEO) {
        headers.add(headerEO);
    }

    public void copy(RequestInfo requestVO) {
        setHttpVerb(requestVO.getHttpVerb());
        setUri(requestVO.getUri());
        setBody(requestVO.getBody());

        getHeaders().clear();
        requestVO.getHeaders().entrySet().forEach(entry -> addHeader(new HeadersEO(entry.getKey(), entry.getValue())));
    }

}
