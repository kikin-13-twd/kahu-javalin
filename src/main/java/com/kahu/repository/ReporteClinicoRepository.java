package com.kahu.repository;

import com.kahu.entity.ReporteClinico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class ReporteClinicoRepository extends BaseRepository<ReporteClinico, Integer> {

    public ReporteClinicoRepository(EntityManagerFactory emf) {
        super(emf, ReporteClinico.class);
    }

    public Optional<ReporteClinico> findByCitaId(Integer idCita) {
        EntityManager em = em();
        try {
            return Optional.of(
                em.createQuery(
                    "FROM ReporteClinico r WHERE r.cita.idCita = :idCita", ReporteClinico.class)
                    .setParameter("idCita", idCita)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            closeIfOwn(em);
        }
    }
}
