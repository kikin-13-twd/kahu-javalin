package com.kahu.service;

import com.kahu.dto.response.RolResponseDTO;
import com.kahu.entity.Rol;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.RolRepository;

import java.util.List;

public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolResponseDTO> listarTodos() {
        return rolRepository.findAll().stream().map(RolResponseDTO::from).toList();
    }

    public RolResponseDTO buscarPorId(Integer id) {
        return RolResponseDTO.from(buscarEntidad(id));
    }

    public RolResponseDTO crear(String nombre) {
        Rol r = new Rol();
        r.setNombreRol(nombre);
        return RolResponseDTO.from(rolRepository.save(r));
    }

    private Rol buscarEntidad(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con id: " + id));
    }
}
