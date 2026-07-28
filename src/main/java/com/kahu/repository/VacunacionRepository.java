package com.kahu.repository;

import com.kahu.entity.Vacunacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class VacunacionRepository extends BaseRepository<Vacunacion, Integer> {

    public VacunacionRepository(EntityManagerFactory emf) {
        super(emf, Vacunacion.class);
    }

    public List<Vacunacion> findByCitaId(Integer idCita) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Vacunacion v WHERE v.cita.idCita = :idCita", Vacunacion.class)
                .setParameter("idCita", idCita)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
