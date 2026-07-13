package com.kahu.controller;

import com.kahu.dto.LoginRequestDTO;
import com.kahu.dto.RegisterRequestDTO;
import com.kahu.service.AuthService;
import com.kahu.util.RequestUtil;
import io.javalin.Javalin;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/api/auth/login", ctx -> {
            LoginRequestDTO dto = RequestUtil.bodyAsValidated(ctx, LoginRequestDTO.class);
            ctx.json(authService.login(dto));
        });

        app.post("/api/auth/register", ctx -> {
            RegisterRequestDTO dto = RequestUtil.bodyAsValidated(ctx, RegisterRequestDTO.class);
            ctx.status(201).json(authService.register(dto));
        });
    }
}
