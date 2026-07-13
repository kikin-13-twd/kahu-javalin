package com.kahu.controller;

import com.kahu.dto.CitaRequestDTO;
import com.kahu.service.CitaService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class CitaController {
    private final CitaService service;
    public CitaController(CitaService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/citas", ctx -> ctx.json(service.listarTodas()));

        app.get("/api/citas/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.get("/api/citas/animal/{idAnimal}", ctx ->
            ctx.json(service.listarPorAnimal(RequestUtil.pathParamAsInt(ctx, "idAnimal"))));

        app.get("/api/citas/personal/{idPersonal}", ctx ->
            ctx.json(service.listarPorPersonal(RequestUtil.pathParamAsInt(ctx, "idPersonal"))));

        app.get("/api/citas/estado/{estado}", ctx ->
            ctx.json(service.listarPorEstado(ctx.pathParam("estado"))));

        app.post("/api/citas", ctx -> {
            CitaRequestDTO dto = RequestUtil.bodyAsValidated(ctx, CitaRequestDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.patch("/api/citas/{id}/estado", ctx -> {
            Integer id = RequestUtil.pathParamAsInt(ctx, "id");
            String estado = RequestUtil.requireQueryParam(ctx, "estado");
            ctx.json(service.actualizarEstado(id, estado));
        });

        app.patch("/api/citas/{idCita}/personal/{idPersonal}", ctx -> {
            Integer idCita = RequestUtil.pathParamAsInt(ctx, "idCita");
            Integer idPersonal = RequestUtil.pathParamAsInt(ctx, "idPersonal");
            ctx.json(service.asignarPersonal(idCita, idPersonal));
        });

        app.delete("/api/citas/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
