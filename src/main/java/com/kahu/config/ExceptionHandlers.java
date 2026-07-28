package com.kahu.config;

import com.kahu.ErrorResponse;
import com.kahu.exception.*;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExceptionHandlers {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlers.class);

    private ExceptionHandlers() {}

    public static void register(Javalin app) {
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
