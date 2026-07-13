package com.kahu.repository;

import com.kahu.entity.TipoConsulta;
import jakarta.persistence.EntityManagerFactory;

public class TipoConsultaRepository extends BaseRepository<TipoConsulta, Integer> {

    public TipoConsultaRepository(EntityManagerFactory emf) {
        super(emf, TipoConsulta.class);
    }
}
