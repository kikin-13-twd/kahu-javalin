package com.kahu.repository;

import com.kahu.entity.Cita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CitaRepository extends BaseRepository<Cita, Integer> {

    public CitaRepository(EntityManagerFactory emf) {
        super(emf, Cita.class);
    }

    public List<Cita> findByDuenioId(Integer idDuenio) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Cita c WHERE c.animal.duenio.idUsuario = :idDuenio", Cita.class)
                .setParameter("idDuenio", idDuenio)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }

    public List<Cita> findByAnimalId(Integer idAnimal) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Cita c WHERE c.animal.idAnimal = :idAnimal", Cita.class)
                .setParameter("idAnimal", idAnimal)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }

    public List<Cita> findByPersonalId(Integer idPersonal) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Cita c WHERE c.personal.idUsuario = :idPersonal", Cita.class)
                .setParameter("idPersonal", idPersonal)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }

    public List<Cita> findByEstado(String estado) {
        EntityManager em = em();
        try {
            return em.createQuery(
                "FROM Cita c WHERE c.estado = :estado", Cita.class)
                .setParameter("estado", estado)
                .getResultList();
        } finally {
            closeIfOwn(em);
        }
    }

    public boolean existsByPersonalAndFechaAndHora(Integer idPersonal, LocalDate fecha, LocalTime hora) {
        EntityManager em = em();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(c) FROM Cita c WHERE c.personal.idUsuario = :idPersonal " +
                "AND c.fecha = :fecha AND c.hora = :hora", Long.class)
                .setParameter("idPersonal", idPersonal)
                .setParameter("fecha", fecha)
                .setParameter("hora", hora)
                .getSingleResult();
            return count > 0;
        } finally {
            closeIfOwn(em);
        }
    }
}
