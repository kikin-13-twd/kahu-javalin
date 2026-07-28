package com.kahu.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * EntityManager por peticion HTTP para soportar FetchType.LAZY durante la serializacion JSON.
 */
public final class PersistenceContext {

    private static final ThreadLocal<EntityManager> CURRENT = new ThreadLocal<>();

    private PersistenceContext() {}

    public static void begin(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        CURRENT.set(em);
    }

    public static EntityManager get() {
        return CURRENT.get();
    }

    public static void end() {
        EntityManager em = CURRENT.get();
        if (em == null) return;
        try {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            CURRENT.remove();
        }
    }
}
