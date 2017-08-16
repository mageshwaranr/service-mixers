package com.ttkey.service.mixers.manifest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackages = { "com.ttkey.service.mixers.manifest.domain." })
@EnableJpaRepositories(basePackages = { "com.ttkey.service.mixers.manifest.repository" })
public class ManifestBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ManifestBootstrap.class, args);
    }
}
