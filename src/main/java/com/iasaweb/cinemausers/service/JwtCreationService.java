package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Service
public class JwtCreationService {
    private final SecretKey signingKey;

    public JwtCreationService(@Value("${secret}") String secret) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String token(User user) {
        ZonedDateTime expirationTime = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .plusHours(24);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRole().name())
                .expiration(Date.from(expirationTime.toInstant()))
                .signWith(signingKey)
                .compact();
    }
}
