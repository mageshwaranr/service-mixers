package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.App;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter
public class AppEO {

    @Id
    private String name;

    private String apikey;

    public AppEO copy(App app) {
        setName(app.getName());
        return this;
    }

    public App toApp() {
        App app = new App();
        app.setName(getName());
        app.setAppKey(getApikey());
        return app;
    }

}
