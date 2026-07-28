package com.kahu.controller;

import com.kahu.dto.AnimalDTO;
import com.kahu.service.AnimalService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class AnimalController {
    private final AnimalService service;
    public AnimalController(AnimalService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/animales", ctx -> ctx.json(service.listarTodos()));

        app.get("/api/animales/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.get("/api/animales/duenio/{idUsuario}", ctx ->
            ctx.json(service.listarPorDuenio(RequestUtil.pathParamAsInt(ctx, "idUsuario"))));

        app.post("/api/animales", ctx -> {
            AnimalDTO dto = RequestUtil.bodyAsValidated(ctx, AnimalDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.put("/api/animales/{id}", ctx -> {
            Integer id = RequestUtil.pathParamAsInt(ctx, "id");
            AnimalDTO dto = RequestUtil.bodyAsValidated(ctx, AnimalDTO.class);
            ctx.json(service.actualizar(id, dto));
        });

        app.delete("/api/animales/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
