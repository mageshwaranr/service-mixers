package com.ttkey.service.mixers.manifest.domain;

import com.ttkey.service.mixers.model.manifest.App;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Entity
@Getter @Setter
public class AppEO {

    @Id
    private String name;

    private String apikey;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    @OrderColumn
    private List<KeyValueEO> configs = new ArrayList<>();

    private void addConfig(KeyValueEO keyValueEO) {
        getConfigs().add(keyValueEO);
    }

    public AppEO copy(App app) {
        setName(app.getName());

        getConfigs().clear();
        app.getConfigs().entrySet().forEach(configEntry -> addConfig(new KeyValueEO(configEntry.getKey(), configEntry.getValue())));

        return this;
    }

    public App toApp() {
        App app = new App();
        app.setName(getName());
        app.setAppKey(getApikey());

        Map<String, String> configs = getConfigs().stream().collect(Collectors.toMap(KeyValueEO::getKey, KeyValueEO::getValue));
        app.setConfigs(configs);
        return app;
    }

}
