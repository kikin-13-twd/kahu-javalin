package com.kahu.controller;

import com.kahu.exception.BusinessException;
import com.kahu.service.VacunacionService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class VacunacionController {
    private final VacunacionService service;
    public VacunacionController(VacunacionService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/vacunas", ctx -> ctx.json(service.listarTodas()));

        app.get("/api/vacunas/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.get("/api/vacunas/cita/{idCita}", ctx ->
            ctx.json(service.listarPorCita(RequestUtil.pathParamAsInt(ctx, "idCita"))));

        app.post("/api/vacunas", ctx -> {
            int idCatalogo = Integer.parseInt(RequestUtil.requireQueryParam(ctx, "idCatalogoVacuna"));
            int idCita = Integer.parseInt(RequestUtil.requireQueryParam(ctx, "idCita"));
            String fechaStr = ctx.queryParam("fechaProxima");
            LocalDate fecha = null;
            if (fechaStr != null && !fechaStr.isBlank()) {
                try {
                    fecha = LocalDate.parse(fechaStr);
                } catch (DateTimeParseException e) {
                    throw new BusinessException("fechaProxima debe tener formato yyyy-MM-dd");
                }
            }
            ctx.status(201).json(service.crear(idCatalogo, idCita, fecha));
        });

        app.patch("/api/vacunas/{id}/fecha-proxima", ctx -> {
            try {
                LocalDate fecha = LocalDate.parse(RequestUtil.requireQueryParam(ctx, "fechaProxima"));
                ctx.json(service.actualizarFechaProxima(RequestUtil.pathParamAsInt(ctx, "id"), fecha));
            } catch (DateTimeParseException e) {
                throw new BusinessException("fechaProxima debe tener formato yyyy-MM-dd");
            }
        });

        app.delete("/api/vacunas/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
