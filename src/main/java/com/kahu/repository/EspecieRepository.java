package com.kahu.repository;

import com.kahu.entity.Especie;
import jakarta.persistence.EntityManagerFactory;

public class EspecieRepository extends BaseRepository<Especie, Integer> {

    public EspecieRepository(EntityManagerFactory emf) {
        super(emf, Especie.class);
    }
}
