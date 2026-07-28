package com.kahu.repository;

import com.kahu.entity.PublicacionAdopcion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PublicacionAdopcionRepository extends BaseRepository<PublicacionAdopcion, Integer> {

    public PublicacionAdopcionRepository(EntityManagerFactory emf) {
        super(emf, PublicacionAdopcion.class);
    }

    public List<PublicacionAdopcion> findByEstado(String estado) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM PublicacionAdopcion p WHERE p.estado = :estado", PublicacionAdopcion.class)
                .setParameter("estado", estado)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
