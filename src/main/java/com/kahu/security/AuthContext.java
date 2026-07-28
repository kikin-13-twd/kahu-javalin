package com.kahu.security;

import com.kahu.exception.ForbiddenException;
import com.kahu.exception.UnauthorizedException;

/**
 * Sesion del usuario autenticado por peticion HTTP (extraida del JWT).
 */
public final class AuthContext {

    public record UserSession(Integer userId, String email, String role) {}

    private static final ThreadLocal<UserSession> CURRENT = new ThreadLocal<>();

    private AuthContext() {}

    public static void set(UserSession session) {
        CURRENT.set(session);
    }

    public static UserSession get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }

    public static Integer requireUserId() {
        UserSession session = get();
        if (session == null) {
            throw new UnauthorizedException("Token de autenticacion requerido");
        }
        return session.userId();
    }

    public static void requireAnyRole(String... roles) {
        UserSession session = get();
        if (session == null) {
            throw new UnauthorizedException("Token de autenticacion requerido");
        }
        for (String role : roles) {
            if (matchesRole(session.role(), role)) {
                return;
            }
        }
        throw new ForbiddenException("No tienes permisos para realizar esta accion");
    }

    public static boolean matchesRole(String actual, String expected) {
        if (actual == null || expected == null) return false;
        return actual.equalsIgnoreCase(expected);
    }
}
