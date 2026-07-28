package com.kahu.security;

import com.kahu.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("clave-secreta-de-prueba-minimo-32-chars", 1);

    @Test
    void generarYValidarToken() {
        String token = jwtUtil.generateToken(42, "test@kahu.com", "Cliente");

        JwtUtil.UserClaims claims = jwtUtil.validateToken(token);

        assertEquals(42, claims.userId());
        assertEquals("test@kahu.com", claims.email());
        assertEquals("Cliente", claims.role());
    }

    @Test
    void tokenInvalido_lanza401() {
        assertThrows(UnauthorizedException.class, () -> jwtUtil.validateToken("token.invalido"));
    }
}
