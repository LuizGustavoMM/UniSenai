package br.senai.LojaProduto.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private String secret;

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(UserDetails userDetails){
        Instant instant = Instant.now();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer("LojaProduto")
                .issuedAt(Date.from(instant))
                .expiration(Date.from(instant.plus(1, ChronoUnit.HOURS)))
                .claim("email", "julio@senai.br")
                .signWith(secretKey)
                .compact();
    }

    public Claims validarToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
