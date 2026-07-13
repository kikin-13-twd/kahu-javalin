package com.kahu.controller;

import com.kahu.dto.ServicioDTO;
import com.kahu.service.ServicioService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class ServicioController {
    private final ServicioService service;
    public ServicioController(ServicioService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/servicios", ctx -> ctx.json(service.listarTodos()));
        app.get("/api/servicios/{id}", ctx -> ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.get("/api/servicios/tipo/{idTipo}", ctx -> ctx.json(service.listarPorTipo(RequestUtil.pathParamAsInt(ctx, "idTipo"))));

        app.post("/api/servicios", ctx -> {
            ServicioDTO dto = RequestUtil.bodyAsValidated(ctx, ServicioDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.put("/api/servicios/{id}", ctx -> {
            ServicioDTO dto = RequestUtil.bodyAsValidated(ctx, ServicioDTO.class);
            ctx.json(service.actualizar(RequestUtil.pathParamAsInt(ctx, "id"), dto));
        });

        app.delete("/api/servicios/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
