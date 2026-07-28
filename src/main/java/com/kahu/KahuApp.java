package com.kahu;

<<<<<<< HEAD
import com.kahu.config.*;
import com.kahu.security.SecurityConfig;
import io.javalin.Javalin;
=======
import com.kahu.config.AppConfig;
import com.kahu.config.CorsConfig;
import com.kahu.config.EnvConfig;
import com.kahu.config.PersistenceContext;
import com.kahu.controller.*;
import com.kahu.exception.*;
import com.kahu.security.AuthContext;
import com.kahu.security.SecurityConfig;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.json.JavalinJackson;
>>>>>>> origin/main
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KahuApp {

    private static final Logger log = LoggerFactory.getLogger(KahuApp.class);

    public static void main(String[] args) {

        EnvConfig env = EnvConfig.get();

        int port = env.getAppPort();


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

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(m -> {
                m.registerModule(new JavaTimeModule());
                m.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }));
            CorsConfig.configure(config, env);
        }).start(port);

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

        SecurityConfig.register(app, appConfig.getJwtUtil());

        new AuthController(appConfig.getAuthService()).registerRoutes(app);
        new RolController(appConfig.getRolService()).registerRoutes(app);
        new UsuarioController(appConfig.getUsuarioService()).registerRoutes(app);
        new EspecieController(appConfig.getEspecieService()).registerRoutes(app);
        new RazaController(appConfig.getRazaService()).registerRoutes(app);
        new AnimalController(appConfig.getAnimalService()).registerRoutes(app);
        new TipoConsultaController(appConfig.getTipoConsultaService()).registerRoutes(app);
        new ServicioController(appConfig.getServicioService()).registerRoutes(app);
        new CitaController(appConfig.getCitaService()).registerRoutes(app);
        new ReporteClinicoController(appConfig.getReporteClinicoService()).registerRoutes(app);
        new CatalogoVacunaController(appConfig.getCatalogoVacunaService()).registerRoutes(app);
        new VacunacionController(appConfig.getVacunacionService()).registerRoutes(app);
        new PublicacionAdopcionController(appConfig.getPublicacionAdopcionService()).registerRoutes(app);
        new SolicitudAdopcionController(appConfig.getSolicitudAdopcionService()).registerRoutes(app);

        registerExceptionHandlers(app);

        log.info("Kahu API corriendo en http://localhost:{} (env={})", port, env.getAppEnv());
    }
    // Configura el manejo centralizado de excepciones de la aplicación,
// devolviendo respuestas JSON consistentes según el tipo de error.
    private static void registerExceptionHandlers(Javalin app) {
        app.exception(UnauthorizedException.class, (e, ctx) -> {
            log.warn("{} {} -> 401: {}", ctx.method(), ctx.path(), e.getMessage());
            ctx.status(401).json(new ErrorResponse("Unauthorized", e.getMessage()));
        });

        app.exception(ForbiddenException.class, (e, ctx) -> {
            log.warn("{} {} -> 403: {}", ctx.method(), ctx.path(), e.getMessage());
            ctx.status(403).json(new ErrorResponse("Forbidden", e.getMessage()));
        });

        app.exception(NotFoundException.class, (e, ctx) -> {
            log.warn("{} {} -> 404: {}", ctx.method(), ctx.path(), e.getMessage());
            ctx.status(404).json(new ErrorResponse("Not Found", e.getMessage()));
        });

        app.exception(BusinessException.class, (e, ctx) -> {
            log.warn("{} {} -> 400: {}", ctx.method(), ctx.path(), e.getMessage());
            ctx.status(400).json(new ErrorResponse("Bad Request", e.getMessage()));
        });

        app.exception(IllegalStateException.class, (e, ctx) -> {
            log.error("{} {} -> 500 config: {}", ctx.method(), ctx.path(), e.getMessage());
            ctx.status(500).json(new ErrorResponse("Configuration Error", e.getMessage()));
        });

        app.exception(PersistenceException.class, (e, ctx) -> {
            log.error("{} {} -> 500 database", ctx.method(), ctx.path(), e);
            ctx.status(500).json(new ErrorResponse("Database Error", "Error al procesar la operacion en base de datos"));
        });

        app.exception(Exception.class, (e, ctx) -> {
            log.error("{} {} -> 500 unexpected", ctx.method(), ctx.path(), e);
            ctx.status(500).json(new ErrorResponse("Internal Server Error", "Ocurrio un error inesperado"));
        });

    }
}
