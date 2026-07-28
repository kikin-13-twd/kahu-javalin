package com.kahu.repository;

import com.kahu.entity.CatalogoVacuna;
import jakarta.persistence.EntityManagerFactory;

public class CatalogoVacunaRepository extends BaseRepository<CatalogoVacuna, Integer> {

    public CatalogoVacunaRepository(EntityManagerFactory emf) {
        super(emf, CatalogoVacuna.class);
    }
}
