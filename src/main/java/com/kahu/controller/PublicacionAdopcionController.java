package com.kahu.controller;

import com.kahu.dto.PublicacionAdopcionDTO;
import com.kahu.service.PublicacionAdopcionService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class PublicacionAdopcionController {
    private final PublicacionAdopcionService service;
    public PublicacionAdopcionController(PublicacionAdopcionService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/adopciones/publicaciones", ctx -> ctx.json(service.listarTodas()));
        app.get("/api/adopciones/publicaciones/disponibles", ctx -> ctx.json(service.listarDisponibles()));
        app.get("/api/adopciones/publicaciones/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.post("/api/adopciones/publicaciones", ctx -> {
            PublicacionAdopcionDTO dto = RequestUtil.bodyAsValidated(ctx, PublicacionAdopcionDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.patch("/api/adopciones/publicaciones/{id}/estado", ctx -> {
            String estado = RequestUtil.requireQueryParam(ctx, "estado");
            ctx.json(service.actualizarEstado(RequestUtil.pathParamAsInt(ctx, "id"), estado));
        });

        app.delete("/api/adopciones/publicaciones/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
