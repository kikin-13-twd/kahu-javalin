package com.kahu.repository;

import com.kahu.config.PersistenceContext;
import com.kahu.exception.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio base generico con operaciones CRUD comunes.
 * Todos los repositorios especificos heredan de esta clase.
 */
public abstract class BaseRepository<T, ID> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    protected BaseRepository(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    protected EntityManager em() {
        EntityManager scoped = PersistenceContext.get();
        return scoped != null ? scoped : emf.createEntityManager();
    }

    private boolean isScoped() {
        return PersistenceContext.get() != null;
    }

    protected void closeIfOwn(EntityManager em) {
        if (!isScoped()) em.close();
    }

    public List<T> findAll() {
        EntityManager em = em();
        try {
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
        } finally {
            if (!isScoped()) em.close();
        }
    }

    public Optional<T> findById(ID id) {
        EntityManager em = em();
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } finally {
            if (!isScoped()) em.close();
        }
    }

    public T save(T entity) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            T saved = em.merge(entity);
            em.getTransaction().commit();
            return saved;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new PersistenceException("Error al guardar entidad", e);
        } finally {
            if (!isScoped()) em.close();
        }
    }

    public void deleteById(ID id) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new PersistenceException("Error al eliminar entidad", e);
        } finally {
            if (!isScoped()) em.close();
        }
    }

    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
