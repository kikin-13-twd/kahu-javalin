package com.kahu.controller;

import com.kahu.dto.ReporteClinicoDTO;
import com.kahu.service.ReporteClinicoService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class ReporteClinicoController {
    private final ReporteClinicoService service;
    public ReporteClinicoController(ReporteClinicoService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/reportes", ctx -> ctx.json(service.listarTodos()));

        app.get("/api/reportes/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.get("/api/reportes/cita/{idCita}", ctx ->
            ctx.json(service.buscarPorCita(RequestUtil.pathParamAsInt(ctx, "idCita"))));

        app.post("/api/reportes", ctx -> {
            ReporteClinicoDTO dto = RequestUtil.bodyAsValidated(ctx, ReporteClinicoDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.put("/api/reportes/{id}", ctx -> {
            ReporteClinicoDTO dto = RequestUtil.bodyAsValidated(ctx, ReporteClinicoDTO.class);
            ctx.json(service.actualizar(RequestUtil.pathParamAsInt(ctx, "id"), dto));
        });
    }
}
