package com.kahu.repository;

import com.kahu.entity.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ServicioRepository extends BaseRepository<Servicio, Integer> {

    public ServicioRepository(EntityManagerFactory emf) {
        super(emf, Servicio.class);
    }

    public List<Servicio> findByTipoConsultaId(Integer idTipo) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Servicio s WHERE s.tipoConsulta.idTipoConsulta = :idTipo", Servicio.class)
                .setParameter("idTipo", idTipo)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
