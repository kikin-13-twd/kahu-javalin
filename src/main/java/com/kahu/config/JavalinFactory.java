package com.kahu.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public final class JavalinFactory {

    private JavalinFactory() {}

    public static Javalin create(EnvConfig env) {
        return Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(m -> {
                m.registerModule(new JavaTimeModule());
                m.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }));
            CorsConfig.configure(config, env);
        });
    }
}
