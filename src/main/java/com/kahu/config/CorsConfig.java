package com.kahu.config;

import io.javalin.config.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public final class CorsConfig {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    private CorsConfig() {}

    public static void configure(JavalinConfig config, EnvConfig env) {
        config.bundledPlugins.enableCors(cors -> {
            if (env.isProduction()) {
                List<String> origins = env.getCorsOrigins();
                if (origins.isEmpty()) {
                    log.warn("APP_ENV=production pero CORS_ORIGINS esta vacio. CORS bloqueara todos los origenes.");
                }
                for (String origin : origins) {
                    cors.addRule(rule -> rule.allowHost(origin));
                }
                log.info("CORS restringido a {} origenes en produccion", origins.size());
            } else {
                cors.addRule(rule -> rule.anyHost());
                log.info("CORS abierto (modo desarrollo)");
            }
        });
    }
}
