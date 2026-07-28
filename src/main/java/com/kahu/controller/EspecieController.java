package com.kahu.controller;

import com.kahu.service.EspecieService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class EspecieController {
    private final EspecieService service;
    public EspecieController(EspecieService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/especies", ctx -> ctx.json(service.listarTodas()));
        app.get("/api/especies/{id}", ctx -> ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.post("/api/especies", ctx -> {
            String nombre = RequestUtil.requireQueryParam(ctx, "nombre");
            ctx.status(201).json(service.crear(nombre));
        });
    }
}
