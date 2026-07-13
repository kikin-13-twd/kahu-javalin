package com.kahu.controller;

import com.kahu.service.RolService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class RolController {
    private final RolService service;
    public RolController(RolService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/roles", ctx -> ctx.json(service.listarTodos()));
        app.get("/api/roles/{id}", ctx -> {
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id")));
        });
        app.post("/api/roles", ctx -> {
            String nombre = RequestUtil.requireQueryParam(ctx, "nombre");
            ctx.status(201).json(service.crear(nombre));
        });
    }
}
