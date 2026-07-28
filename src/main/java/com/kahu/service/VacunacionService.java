package com.kahu.service;

import com.kahu.dto.response.VacunacionResponseDTO;
import com.kahu.entity.CatalogoVacuna;
import com.kahu.entity.Cita;
import com.kahu.entity.Vacunacion;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.CatalogoVacunaRepository;
import com.kahu.repository.CitaRepository;
import com.kahu.repository.VacunacionRepository;

import java.time.LocalDate;
import java.util.List;

public class VacunacionService {
    private final VacunacionRepository vacunaRepo;
    private final CatalogoVacunaRepository catalogoRepo;
    private final CitaRepository citaRepo;

    public VacunacionService(VacunacionRepository vacunaRepo, CatalogoVacunaRepository catalogoRepo,
                             CitaRepository citaRepo) {
        this.vacunaRepo = vacunaRepo;
        this.catalogoRepo = catalogoRepo;
        this.citaRepo = citaRepo;
    }

    public List<VacunacionResponseDTO> listarTodas() {
        return vacunaRepo.findAll().stream().map(VacunacionResponseDTO::from).toList();
    }

    public List<VacunacionResponseDTO> listarPorCita(Integer idCita) {
        return vacunaRepo.findByCitaId(idCita).stream().map(VacunacionResponseDTO::from).toList();
    }

    public VacunacionResponseDTO buscarPorId(Integer id) {
        return VacunacionResponseDTO.from(buscarEntidad(id));
    }

    public VacunacionResponseDTO crear(Integer idCatalogo, Integer idCita, LocalDate fechaProxima) {
        CatalogoVacuna cv = catalogoRepo.findById(idCatalogo)
                .orElseThrow(() -> new NotFoundException("Vacuna de catalogo no encontrada"));
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        Vacunacion v = new Vacunacion();
        v.setCatalogoVacuna(cv);
        v.setCita(cita);
        v.setFechaProxima(fechaProxima);
        return VacunacionResponseDTO.from(vacunaRepo.save(v));
    }

    public VacunacionResponseDTO actualizarFechaProxima(Integer id, LocalDate fechaProxima) {
        Vacunacion v = buscarEntidad(id);
        v.setFechaProxima(fechaProxima);
        return VacunacionResponseDTO.from(vacunaRepo.save(v));
    }

    public void eliminar(Integer id) {
        if (!vacunaRepo.existsById(id)) {
            throw new NotFoundException("Vacunacion no encontrada con id: " + id);
        }
        vacunaRepo.deleteById(id);
    }

    private Vacunacion buscarEntidad(Integer id) {
        return vacunaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vacunacion no encontrada con id: " + id));
    }
}
