package com.kahu.service;

import com.kahu.dto.LoginRequestDTO;
import com.kahu.dto.RegisterRequestDTO;
import com.kahu.entity.Rol;
import com.kahu.entity.Usuario;
import com.kahu.exception.BusinessException;
import com.kahu.exception.UnauthorizedException;
import com.kahu.repository.RolRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.JwtUtil;
import com.kahu.security.RoleConstants;
import com.kahu.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RolRepository rolRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AuthService authService;

    private Usuario usuario;
    private Rol rolCliente;

    @BeforeEach
    void setUp() {
        rolCliente = new Rol(3, RoleConstants.CLIENTE);
        usuario = new Usuario(1, "Juan", "Perez", "juan@test.com",
                PasswordUtil.hash("password123"), "555", null, LocalDate.now(), rolCliente);
    }

    @Test
    void login_exitoso_devuelveToken() {
        when(usuarioRepository.findByEmailForAuth("juan@test.com")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generateToken(1, "juan@test.com", RoleConstants.CLIENTE)).thenReturn("token123");

        var result = authService.login(new LoginRequestDTO("juan@test.com", "password123"));

        assertEquals("token123", result.getToken());
        assertEquals("Bearer", result.getTipo());
        assertEquals("juan@test.com", result.getUsuario().getEmail());
    }

    @Test
    void login_emailInvalido_lanza401() {
        when(usuarioRepository.findByEmailForAuth("no@test.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> authService.login(new LoginRequestDTO("no@test.com", "pass")));
    }

    @Test
    void register_emailDuplicado_lanza400() {
        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(true);

        var dto = new RegisterRequestDTO("Juan", "Perez", "juan@test.com", "123456", "555", null);
        assertThrows(BusinessException.class, () -> authService.register(dto));
    }

    @Test
    void register_exitoso_devuelveToken() {
        when(usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(rolRepository.findByNombreRol(RoleConstants.CLIENTE)).thenReturn(Optional.of(rolCliente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setIdUsuario(5);
            return u;
        });
        when(jwtUtil.generateToken(5, "nuevo@test.com", RoleConstants.CLIENTE)).thenReturn("newtoken");

        var dto = new RegisterRequestDTO("Nuevo", "User", "nuevo@test.com", "123456", "555", null);
        var result = authService.register(dto);

        assertEquals("newtoken", result.getToken());
        assertEquals(RoleConstants.CLIENTE, result.getUsuario().getNombreRol());
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
