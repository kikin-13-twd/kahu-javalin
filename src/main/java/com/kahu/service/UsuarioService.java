package com.kahu.service;

import com.kahu.dto.UsuarioDTO;
import com.kahu.dto.UsuarioResponseDTO;
import com.kahu.dto.UsuarioUpdateDTO;
import com.kahu.entity.Rol;
import com.kahu.entity.Usuario;
import com.kahu.exception.BusinessException;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.RolRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.util.PasswordUtil;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::from).toList();
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        return UsuarioResponseDTO.from(buscarEntidadPorId(id));
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        return UsuarioResponseDTO.from(
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email))
        );
    }

    public UsuarioResponseDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setContrasena(PasswordUtil.hash(dto.getContrasena()));
        u.setTelefono(dto.getTelefono());
        u.setDireccion(dto.getDireccion());
        u.setFechaRegistro(LocalDate.now());
        u.setRol(rol);
        return UsuarioResponseDTO.from(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario u = buscarEntidadPorId(id);
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setTelefono(dto.getTelefono());
        u.setDireccion(dto.getDireccion());
        u.setRol(rol);
        return UsuarioResponseDTO.from(usuarioRepository.save(u));
    }

    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidadPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + id));
    }
}
