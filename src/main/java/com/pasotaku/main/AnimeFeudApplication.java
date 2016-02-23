package com.pasotaku.main;

import com.pasotaku.animefeud.health.TemplateHealthCheck;
import com.pasotaku.animefeud.resources.AuthenticationResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AnimeFeudApplication extends Application<AnimeFeudConfiguration> {

    public static void main(String[] args) throws Exception {
        new AnimeFeudApplication().run(args);
    }

    public String getName(AnimeFeudConfiguration configuration) {
        return configuration.getApplicationName();
    }

    @Override
    public void initialize(Bootstrap<AnimeFeudConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(AnimeFeudConfiguration configuration,
                    Environment environment) {
        final AuthenticationResource resource = new AuthenticationResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);


    }

}