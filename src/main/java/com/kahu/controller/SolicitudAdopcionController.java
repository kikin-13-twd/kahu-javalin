package com.kahu.controller;

import com.kahu.dto.SolicitudAdopcionDTO;
import com.kahu.service.SolicitudAdopcionService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class SolicitudAdopcionController {
    private final SolicitudAdopcionService service;
    public SolicitudAdopcionController(SolicitudAdopcionService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/adopciones/solicitudes", ctx -> ctx.json(service.listarTodas()));
        app.get("/api/adopciones/solicitudes/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.get("/api/adopciones/solicitudes/usuario/{idUsuario}", ctx ->
            ctx.json(service.listarPorUsuario(RequestUtil.pathParamAsInt(ctx, "idUsuario"))));
        app.get("/api/adopciones/solicitudes/publicacion/{idPublicacion}", ctx ->
            ctx.json(service.listarPorPublicacion(RequestUtil.pathParamAsInt(ctx, "idPublicacion"))));

        app.post("/api/adopciones/solicitudes", ctx -> {
            SolicitudAdopcionDTO dto = RequestUtil.bodyAsValidated(ctx, SolicitudAdopcionDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.patch("/api/adopciones/solicitudes/{id}/estado", ctx -> {
            String estado = RequestUtil.requireQueryParam(ctx, "estado");
            ctx.json(service.actualizarEstado(RequestUtil.pathParamAsInt(ctx, "id"), estado));
        });
    }
}
