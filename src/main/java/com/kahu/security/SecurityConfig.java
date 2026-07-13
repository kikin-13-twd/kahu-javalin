package com.kahu.security;

import com.kahu.exception.UnauthorizedException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

/**
 * Middleware de autenticacion JWT y autorizacion por roles.
 */
public final class SecurityConfig {

    private SecurityConfig() {}

    public static void register(Javalin app, JwtUtil jwtUtil) {
        app.before("/api/*", ctx -> {
            if (ctx.method() == HandlerType.OPTIONS) return;
            if (isPublicPath(ctx.path())) return;

            authenticate(ctx, jwtUtil);
            authorize(ctx);
        });
    }

    private static boolean isPublicPath(String path) {
        return path.equals("/api/auth/login")
                || path.equals("/api/auth/register")
                || path.equals("/api/adopciones/publicaciones/disponibles");
    }

    private static void authenticate(Context ctx, JwtUtil jwtUtil) {
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token de autenticacion requerido");
        }

        String token = header.substring("Bearer ".length()).trim();
        if (token.isBlank()) {
            throw new UnauthorizedException("Token de autenticacion requerido");
        }

        JwtUtil.UserClaims claims = jwtUtil.validateToken(token);
        AuthContext.set(new AuthContext.UserSession(claims.userId(), claims.email(), claims.role()));
    }

    private static void authorize(Context ctx) {
        String path = ctx.path();
        HandlerType method = ctx.method();

        if (method == HandlerType.PATCH
                && path.startsWith("/api/adopciones/solicitudes/")
                && path.endsWith("/estado")) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        if (isAdminOnlyPath(path)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        if (isWriteMethod(method) && isAdminWritePath(path)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        if (isWriteMethod(method) && isVeterinarioWritePath(path, method)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN, RoleConstants.VETERINARIO);
        }
    }

    private static boolean isAdminOnlyPath(String path) {
        return path.startsWith("/api/roles")
                || path.startsWith("/api/usuarios");
    }

    private static boolean isAdminWritePath(String path) {
        return path.startsWith("/api/especies")
                || path.startsWith("/api/razas")
                || path.startsWith("/api/tipos-consulta")
                || path.startsWith("/api/servicios")
                || path.startsWith("/api/vacunas/catalogo")
                || path.startsWith("/api/adopciones/publicaciones");
    }

    private static boolean isVeterinarioWritePath(String path, HandlerType method) {
        if (path.startsWith("/api/reportes")) {
            return true;
        }
        if (path.startsWith("/api/vacunas") && !path.startsWith("/api/vacunas/catalogo")) {
            return true;
        }
        if (path.startsWith("/api/citas")) {
            return method == HandlerType.PATCH || method == HandlerType.DELETE;
        }
        return false;
    }

    private static boolean isWriteMethod(HandlerType method) {
        return method == HandlerType.POST
                || method == HandlerType.PUT
                || method == HandlerType.PATCH
                || method == HandlerType.DELETE;
    }
}
