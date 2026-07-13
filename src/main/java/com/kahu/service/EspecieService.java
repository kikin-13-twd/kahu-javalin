package com.kahu.service;

import com.kahu.dto.response.EspecieResponseDTO;
import com.kahu.entity.Especie;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.EspecieRepository;

import java.util.List;

public class EspecieService {
    private final EspecieRepository repo;

    public EspecieService(EspecieRepository repo) { this.repo = repo; }

    public List<EspecieResponseDTO> listarTodas() {
        return repo.findAll().stream().map(EspecieResponseDTO::from).toList();
    }

    public EspecieResponseDTO buscarPorId(Integer id) {
        return EspecieResponseDTO.from(buscarEntidad(id));
    }

    public EspecieResponseDTO crear(String nombre) {
        Especie e = new Especie();
        e.setNombreEspecie(nombre);
        return EspecieResponseDTO.from(repo.save(e));
    }

    private Especie buscarEntidad(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Especie no encontrada con id: " + id));
    }
}
