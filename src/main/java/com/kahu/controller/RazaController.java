package com.kahu.controller;

import com.kahu.exception.BusinessException;
import com.kahu.service.RazaService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class RazaController {
    private final RazaService service;
    public RazaController(RazaService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/razas", ctx -> ctx.json(service.listarTodas()));
        app.get("/api/razas/{id}", ctx -> ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));
        app.get("/api/razas/especie/{idEspecie}", ctx ->
            ctx.json(service.listarPorEspecie(RequestUtil.pathParamAsInt(ctx, "idEspecie"))));

        app.post("/api/razas", ctx -> {
            String nombre = RequestUtil.requireQueryParam(ctx, "nombre");
            int idEspecie;
            try {
                idEspecie = Integer.parseInt(RequestUtil.requireQueryParam(ctx, "idEspecie"));
            } catch (NumberFormatException e) {
                throw new BusinessException("idEspecie debe ser un numero entero valido");
            }
            ctx.status(201).json(service.crear(nombre, idEspecie));
        });
    }
}
