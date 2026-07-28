package com.kahu.repository;

import com.kahu.entity.SolicitudAdopcion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SolicitudAdopcionRepository extends BaseRepository<SolicitudAdopcion, Integer> {

    public SolicitudAdopcionRepository(EntityManagerFactory emf) {
        super(emf, SolicitudAdopcion.class);
    }

    public List<SolicitudAdopcion> findByUsuarioId(Integer idUsuario) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM SolicitudAdopcion s WHERE s.usuarioInteresado.idUsuario = :idUsuario",
                SolicitudAdopcion.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }

    public List<SolicitudAdopcion> findByPublicacionId(Integer idPublicacion) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM SolicitudAdopcion s WHERE s.publicacion.idPublicacion = :idPublicacion",
                SolicitudAdopcion.class)
                .setParameter("idPublicacion", idPublicacion)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
