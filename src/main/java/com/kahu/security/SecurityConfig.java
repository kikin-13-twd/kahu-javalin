package com.kahu.security;

import com.kahu.exception.UnauthorizedException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

/**
 * Middleware de autenticacion JWT y autorizacion por roles.
 *
 * Rutas publicas (sin token):
 *   POST /api/auth/login
 *   POST /api/auth/register
 *   GET  /api/adopciones/publicaciones/disponibles
 *   GET  /api/adopciones/publicaciones/{id}  (solo lectura)
 *
 * TODO lo demas requiere token JWT valido.
 */
public final class SecurityConfig {

    private SecurityConfig() {}

    public static void register(Javalin app, JwtUtil jwtUtil) {
        app.before("/api/*", ctx -> {
            // Preflight CORS siempre permitido
            if (ctx.method() == HandlerType.OPTIONS) return;
            // Rutas publicas
            if (isPublicPath(ctx.path(), ctx.method())) return;
            // Todas las demas requieren token
            authenticate(ctx, jwtUtil);
            authorize(ctx);
        });
    }

    // ── Rutas públicas ────────────────────────────────────────────────────────

    private static boolean isPublicPath(String path, HandlerType method) {
        if (path.equals("/api/auth/login"))    return true;
        if (path.equals("/api/auth/register")) return true;
        // Adopciones: solo GET es publico
        if (method == HandlerType.GET) {
            if (path.equals("/api/adopciones/publicaciones/disponibles")) return true;
            if (path.matches("/api/adopciones/publicaciones/\\d+"))       return true;
        }
        return false;
    }

    // ── Autenticación ─────────────────────────────────────────────────────────

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
        AuthContext.set(new AuthContext.UserSession(
                claims.userId(), claims.email(), claims.role()));
    }

    // ── Autorización por rol ──────────────────────────────────────────────────

    private static void authorize(Context ctx) {
        String      path   = ctx.path();
        HandlerType method = ctx.method();

        // PATCH estado de solicitudes → solo Admin
        if (method == HandlerType.PATCH
                && path.startsWith("/api/adopciones/solicitudes/")
                && path.endsWith("/estado")) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        // Rutas exclusivas de Admin (cualquier metodo)
        if (isAdminOnlyPath(path)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        // Escritura solo Admin
        if (isWriteMethod(method) && isAdminWritePath(path)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN);
            return;
        }

        // Escritura Veterinario o Admin
        if (isWriteMethod(method) && isVeterinarioWritePath(path, method)) {
            AuthContext.requireAnyRole(RoleConstants.ADMIN, RoleConstants.VETERINARIO);
            return;
        }

        // GET con token valido → permitido para cualquier rol
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
        if (path.startsWith("/api/reportes"))   return true;
        if (path.startsWith("/api/vacunas") && !path.startsWith("/api/vacunas/catalogo")) return true;
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
