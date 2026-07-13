package com.kahu.service;

import com.kahu.dto.LoginRequestDTO;
import com.kahu.dto.LoginResponseDTO;
import com.kahu.dto.RegisterRequestDTO;
import com.kahu.dto.UsuarioResponseDTO;
import com.kahu.entity.Rol;
import com.kahu.entity.Usuario;
import com.kahu.exception.BusinessException;
import com.kahu.exception.NotFoundException;
import com.kahu.exception.UnauthorizedException;
import com.kahu.repository.RolRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.JwtUtil;
import com.kahu.security.RoleConstants;
import com.kahu.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmailForAuth(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        if (!PasswordUtil.verify(dto.getContrasena(), usuario.getContrasena())) {
            log.warn("Intento de login fallido para email: {}", dto.getEmail());
            throw new UnauthorizedException("Credenciales invalidas");
        }

        log.info("Login exitoso para usuario id={}", usuario.getIdUsuario());
        return buildLoginResponse(usuario);
    }

    public LoginResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        Rol rolCliente = rolRepository.findByNombreRol(RoleConstants.CLIENTE)
                .orElseThrow(() -> new NotFoundException(
                        "Rol Cliente no encontrado. Contacta al administrador del sistema"));

        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setContrasena(PasswordUtil.hash(dto.getContrasena()));
        u.setTelefono(dto.getTelefono());
        u.setDireccion(dto.getDireccion());
        u.setFechaRegistro(LocalDate.now());
        u.setRol(rolCliente);

        Usuario saved = usuarioRepository.save(u);
        log.info("Registro exitoso para usuario id={}", saved.getIdUsuario());
        return buildLoginResponse(saved);
    }

    private LoginResponseDTO buildLoginResponse(Usuario usuario) {
        String token = jwtUtil.generateToken(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getRol().getNombreRol()
        );
        return new LoginResponseDTO(token, "Bearer", UsuarioResponseDTO.from(usuario));
    }
}
