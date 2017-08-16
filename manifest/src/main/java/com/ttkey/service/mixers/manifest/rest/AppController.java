package com.ttkey.service.mixers.manifest.rest;

import com.ttkey.service.mixers.manifest.domain.AppEO;
import com.ttkey.service.mixers.manifest.repository.AppRepository;
import com.ttkey.service.mixers.model.manifest.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppRepository appRepository;

    @RequestMapping(path = "/", method = POST)
    App createApp(@RequestBody App app) {
        return appRepository.save(new AppEO().copy(app)).toApp();
    }

    @RequestMapping(path = "/{appName}", method = GET)
    App getApp(@PathVariable String appName) {
        AppEO appEO = appRepository.findOne(appName);
        if (appEO == null)
            throw new IllegalArgumentException("No App with name '" + appName + "'");
        return appEO.toApp();
    }
}
