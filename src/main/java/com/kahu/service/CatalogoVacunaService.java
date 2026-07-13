package com.kahu.service;

import com.kahu.dto.response.CatalogoVacunaResponseDTO;
import com.kahu.entity.CatalogoVacuna;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.CatalogoVacunaRepository;

import java.util.List;

public class CatalogoVacunaService {
    private final CatalogoVacunaRepository repo;

    public CatalogoVacunaService(CatalogoVacunaRepository repo) { this.repo = repo; }

    public List<CatalogoVacunaResponseDTO> listarTodas() {
        return repo.findAll().stream().map(CatalogoVacunaResponseDTO::from).toList();
    }

    public CatalogoVacunaResponseDTO buscarPorId(Integer id) {
        return CatalogoVacunaResponseDTO.from(buscarEntidad(id));
    }

    public CatalogoVacunaResponseDTO crear(String nombreVacuna) {
        CatalogoVacuna cv = new CatalogoVacuna();
        cv.setNombreVacuna(nombreVacuna);
        return CatalogoVacunaResponseDTO.from(repo.save(cv));
    }

    private CatalogoVacuna buscarEntidad(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vacuna no encontrada con id: " + id));
    }
}
