package com.kahu.config;

import com.kahu.security.AuthContext;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MiddlewareConfig {

    private static final Logger log = LoggerFactory.getLogger(MiddlewareConfig.class);

    private MiddlewareConfig() {}

    public static void register(Javalin app, AppConfig appConfig) {
        app.before(ctx -> {
            if (ctx.method() != HandlerType.OPTIONS) {
                log.debug("{} {}", ctx.method(), ctx.path());
            }
            PersistenceContext.begin(appConfig.getEmf());
        });

        app.after(ctx -> {
            if (ctx.method() != HandlerType.OPTIONS) {
                log.info("{} {} -> {}", ctx.method(), ctx.path(), ctx.status());
            }
            PersistenceContext.end();
            AuthContext.clear();
        });
    }
}
