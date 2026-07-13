package com.kahu.repository;

import com.kahu.entity.Raza;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RazaRepository extends BaseRepository<Raza, Integer> {

    public RazaRepository(EntityManagerFactory emf) {
        super(emf, Raza.class);
    }

    public List<Raza> findByEspecieId(Integer idEspecie) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Raza r WHERE r.especie.idEspecie = :idEspecie", Raza.class)
                .setParameter("idEspecie", idEspecie)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
