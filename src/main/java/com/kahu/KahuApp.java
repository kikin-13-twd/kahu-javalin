package com.kahu;

import com.kahu.config.*;
import com.kahu.security.SecurityConfig;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KahuApp {

    private static final Logger log = LoggerFactory.getLogger(KahuApp.class);

    public static void main(String[] args) {

        EnvConfig env = EnvConfig.get();
        AppConfig appConfig = new AppConfig();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Cerrando aplicacion...");
            appConfig.shutdown();
        }));

        Javalin app = JavalinFactory.create(env);

        MiddlewareConfig.register(app, appConfig);
        SecurityConfig.register(app, appConfig.getJwtUtil());
        ControllerRegistry.register(app, appConfig);
        ExceptionHandlers.register(app);

        app.start(env.getAppPort());

        log.info("Kahu API corriendo en http://localhost:{} (env={})",
                env.getAppPort(), env.getAppEnv());
    }
}
