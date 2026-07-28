package com.kahu.repository;

import com.kahu.entity.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AnimalRepository extends BaseRepository<Animal, Integer> {

    public AnimalRepository(EntityManagerFactory emf) {
        super(emf, Animal.class);
    }

    public List<Animal> findByDuenioId(Integer idUsuario) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Animal a WHERE a.duenio.idUsuario = :idUsuario", Animal.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }
}
