package com.kahu.security;

import com.kahu.entity.Animal;
import com.kahu.entity.Cita;
import com.kahu.exception.ForbiddenException;

/**
 * Verifica que un cliente solo acceda a sus propios recursos.
 */
public final class OwnershipUtil {

    private OwnershipUtil() {}

    public static boolean isCliente() {
        AuthContext.UserSession session = AuthContext.get();
        return session != null && AuthContext.matchesRole(session.role(), RoleConstants.CLIENTE);
    }

    public static boolean isAdminOrVeterinario() {
        AuthContext.UserSession session = AuthContext.get();
        if (session == null) return false;
        return AuthContext.matchesRole(session.role(), RoleConstants.ADMIN)
                || AuthContext.matchesRole(session.role(), RoleConstants.VETERINARIO);
    }

    public static void requireAnimalOwner(Animal animal) {
        if (!isCliente()) return;
        Integer userId = AuthContext.requireUserId();
        if (animal.getDuenio() == null || !userId.equals(animal.getDuenio().getIdUsuario())) {
            throw new ForbiddenException("No tienes permiso para acceder a este animal");
        }
    }

    public static void requireCitaOwner(Cita cita) {
        if (!isCliente()) return;
        Integer userId = AuthContext.requireUserId();
        if (cita.getAnimal() == null
                || cita.getAnimal().getDuenio() == null
                || !userId.equals(cita.getAnimal().getDuenio().getIdUsuario())) {
            throw new ForbiddenException("No tienes permiso para acceder a esta cita");
        }
    }

    public static void requireSameUser(Integer targetUserId) {
        if (!isCliente()) return;
        if (!AuthContext.requireUserId().equals(targetUserId)) {
            throw new ForbiddenException("No tienes permiso para acceder a este recurso");
        }
    }
}
