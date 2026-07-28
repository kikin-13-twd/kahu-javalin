package com.kahu.service;

import com.kahu.dto.response.TipoConsultaResponseDTO;
import com.kahu.entity.Rol;
import com.kahu.entity.TipoConsulta;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.RolRepository;
import com.kahu.repository.TipoConsultaRepository;

import java.util.List;

public class TipoConsultaService {
    private final TipoConsultaRepository repo;
    private final RolRepository rolRepo;

    public TipoConsultaService(TipoConsultaRepository repo, RolRepository rolRepo) {
        this.repo = repo;
        this.rolRepo = rolRepo;
    }

    public List<TipoConsultaResponseDTO> listarTodos() {
        return repo.findAll().stream().map(TipoConsultaResponseDTO::from).toList();
    }

    public TipoConsultaResponseDTO buscarPorId(Integer id) {
        return TipoConsultaResponseDTO.from(buscarEntidad(id));
    }

    public TipoConsultaResponseDTO crear(String nombreTipo, Integer idRolRequerido) {
        Rol rol = rolRepo.findById(idRolRequerido)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        TipoConsulta tc = new TipoConsulta();
        tc.setNombreTipo(nombreTipo);
        tc.setRolRequerido(rol);
        return TipoConsultaResponseDTO.from(repo.save(tc));
    }

    private TipoConsulta buscarEntidad(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("TipoConsulta no encontrado con id: " + id));
    }
}
