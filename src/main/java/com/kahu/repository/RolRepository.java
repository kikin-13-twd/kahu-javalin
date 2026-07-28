package com.kahu.repository;

import com.kahu.entity.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class RolRepository extends BaseRepository<Rol, Integer> {

    public RolRepository(EntityManagerFactory emf) {
        super(emf, Rol.class);
    }

    public Optional<Rol> findByNombreRol(String nombreRol) {
        EntityManager em = em();
        try {
            return Optional.of(
                    em.createQuery("FROM Rol r WHERE LOWER(r.nombreRol) = LOWER(:nombre)", Rol.class)
                            .setParameter("nombre", nombreRol)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            closeIfOwn(em);
        }
    }
}
