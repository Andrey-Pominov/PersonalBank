package com.example.personal.bank.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String generateToken(Long userId) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        SecretKeySpec key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        SecretKeySpec key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());

        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);

    }

    public boolean validateToken(String token) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            SecretKeySpec key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
