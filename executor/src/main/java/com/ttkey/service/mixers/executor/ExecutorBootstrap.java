package com.ttkey.service.mixers.executor;

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
        super.initialize(bootstrap);
    }

    @Override
    public void run(CustomConfiguration configuration, Environment environment) throws Exception {
        FunctionExecutorService resource = new FunctionExecutorService();
        environment.jersey().register(resource);
    }
}
