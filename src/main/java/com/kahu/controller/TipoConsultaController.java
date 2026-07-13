package com.kahu.controller;

import com.kahu.exception.BusinessException;
import com.kahu.service.TipoConsultaService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class TipoConsultaController {
    private final TipoConsultaService service;
    public TipoConsultaController(TipoConsultaService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/tipos-consulta", ctx -> ctx.json(service.listarTodos()));
        app.get("/api/tipos-consulta/{id}", ctx -> ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.post("/api/tipos-consulta", ctx -> {
            String nombre = RequestUtil.requireQueryParam(ctx, "nombreTipo");
            int idRol;
            try {
                idRol = Integer.parseInt(RequestUtil.requireQueryParam(ctx, "idRolRequerido"));
            } catch (NumberFormatException e) {
                throw new BusinessException("idRolRequerido debe ser un numero entero valido");
            }
            ctx.status(201).json(service.crear(nombre, idRol));
        });
    }
}
