package com.iasaweb.cinemausers.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
public class JwtCreationService {
    private final SecretKey signingKey;

    public JwtCreationService(@Value("${secret}") String secret) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String token(UserDetails user) {
        ZonedDateTime expirationTime = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .plusHours(24);

        List<String> authorities = serializeAuthorities(user.getAuthorities());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", authorities)
                .expiration(Date.from(expirationTime.toInstant()))
                .signWith(signingKey)
                .compact();
    }

    private List<String> serializeAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
