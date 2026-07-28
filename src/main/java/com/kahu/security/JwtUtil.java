package com.kahu.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kahu.exception.UnauthorizedException;

import java.util.Date;

public class JwtUtil {

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_EMAIL = "email";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMs;

    public JwtUtil(String secret, long expirationHours) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationMs = expirationHours * 60L * 60L * 1000L;
    }

    public String generateToken(Integer userId, String email, String role) {
        Date expiresAt = new Date(System.currentTimeMillis() + expirationMs);
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim(CLAIM_EMAIL, email)
                .withClaim(CLAIM_ROLE, role)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public UserClaims validateToken(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return new UserClaims(
                    Integer.parseInt(jwt.getSubject()),
                    jwt.getClaim(CLAIM_EMAIL).asString(),
                    jwt.getClaim(CLAIM_ROLE).asString()
            );
        } catch (JWTVerificationException | NumberFormatException e) {
            throw new UnauthorizedException("Token invalido o expirado");
        }
    }

    public record UserClaims(Integer userId, String email, String role) {}
}
