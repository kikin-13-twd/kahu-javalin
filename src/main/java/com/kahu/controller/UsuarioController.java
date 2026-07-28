package com.kahu.controller;

import com.kahu.dto.UsuarioDTO;
import com.kahu.dto.UsuarioUpdateDTO;
import com.kahu.service.UsuarioService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }

    public void registerRoutes(Javalin app) {
        app.get("/api/usuarios", ctx -> ctx.json(service.listarTodos()));

        app.get("/api/usuarios/{id}", ctx ->
            ctx.json(service.buscarPorId(RequestUtil.pathParamAsInt(ctx, "id"))));

        app.get("/api/usuarios/email/{email}", ctx ->
            ctx.json(service.buscarPorEmail(ctx.pathParam("email"))));

        app.post("/api/usuarios", ctx -> {
            UsuarioDTO dto = RequestUtil.bodyAsValidated(ctx, UsuarioDTO.class);
            ctx.status(201).json(service.crear(dto));
        });

        app.put("/api/usuarios/{id}", ctx -> {
            Integer id = RequestUtil.pathParamAsInt(ctx, "id");
            UsuarioUpdateDTO dto = RequestUtil.bodyAsValidated(ctx, UsuarioUpdateDTO.class);
            ctx.json(service.actualizar(id, dto));
        });

        app.delete("/api/usuarios/{id}", ctx -> {
            service.eliminar(RequestUtil.pathParamAsInt(ctx, "id"));
            ctx.status(204);
        });
    }
}
