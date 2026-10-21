package com.acedigital.smartresolve.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    // Minimum 256-bit safe signing key sequence for local development sessions
    private static final String SECRET_SIGNING_KEY = "smartresolve_ai_secure_cryptographic_jwt_signing_key_2026_token_string";
    private static final long JWT_EXPIRATION_THRESHOLD_MS = 1000 * 60 * 60 * 24; // 24-Hour Session Expiry Window

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_SIGNING_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Generates a secure, role-mapped cryptographic session token string
    public String generateToken(String email, String role) {
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("role", role);

        return Jwts.builder()
                .claims(customClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_THRESHOLD_MS))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    // 2. Extracts the unique user email string from the encrypted claims payload
    public String extractEmail(String encryptionToken) {
        return extractClaim(encryptionToken, Claims::getSubject);
    }

    // 3. Performs signature checking and session expiration validation
    public boolean isTokenValid(String token, String userEmail) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
