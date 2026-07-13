package com.kahu.repository;

import com.kahu.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario, Integer> {

    public UsuarioRepository(EntityManagerFactory emf) {
        super(emf, Usuario.class);
    }

    public Optional<Usuario> findByEmail(String email) {
        EntityManager em = em();
        try {
            return Optional.of(
                em.createQuery("FROM Usuario u WHERE u.email = :email", Usuario.class)
                  .setParameter("email", email)
                  .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            closeIfOwn(em);
        }
    }

    public Optional<Usuario> findByEmailForAuth(String email) {
        EntityManager em = em();
        try {
            return Optional.of(
                em.createQuery(
                    "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.email = :email", Usuario.class)
                  .setParameter("email", email)
                  .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            closeIfOwn(em);
        }
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
