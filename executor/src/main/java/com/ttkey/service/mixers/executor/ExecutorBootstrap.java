package com.ttkey.service.mixers.executor;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.ttkey.service.mixers.executor.resources.FunctionExecutorService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExecutorBootstrap  extends Application<CustomConfiguration> {

    @Override
    public String getName() {
        return "Service Mixer Function Executor";
    }

    @Override
    public void initialize(Bootstrap<CustomConfiguration> bootstrap) {

        GuiceBundle<CustomConfiguration> guiceBundle = GuiceBundle.<CustomConfiguration>newBuilder()
            .addModule(new ExecutorModule())
            .setConfigClass(CustomConfiguration.class)
            .enableAutoConfig("com.ttkey.service.mixers.executor.resources")
            .build();
        bootstrap.addBundle(guiceBundle);
        super.initialize(bootstrap);
    }

    @Override
    public void run(CustomConfiguration configuration, Environment environment) throws Exception {
    }

    public static void main(String[] args) throws Exception {
        new ExecutorBootstrap().run(args);
    }
}
