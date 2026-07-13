package com.kahu.controller;

import com.kahu.service.CatalogoVacunaService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class CatalogoVacunaController {
    private final CatalogoVacunaService service;
    public CatalogoVacunaController(CatalogoVacunaService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/vacunas/catalogo", ctx -> ctx.json(service.listarTodas()));
        app.get("/api/vacunas/catalogo/{id}", ctx -> ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.post("/api/vacunas/catalogo", ctx -> {
            String nombre = RequestUtil.requireQueryParam(ctx, "nombreVacuna");
            ctx.status(201).json(service.crear(nombre));
        });
    }
}
