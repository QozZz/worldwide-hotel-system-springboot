package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.exception.JwtAuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtProvider {

    private static final String CLAIMS_ROLES_KEY = "auth";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";
    private static final String MESSAGE_WRONG_ROLE = "User must have role!";
    private String encodedSecretKey;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private String expirationTimeHours;

    @PostConstruct
    private void init() {
        encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, Collection<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(CLAIMS_ROLES_KEY, roles.stream().reduce((role, prev) -> prev + "," + role).orElseThrow(() ->
                new IllegalArgumentException(MESSAGE_WRONG_ROLE)));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(Long.parseLong(expirationTimeHours))))
                .signWith(SignatureAlgorithm.HS512, encodedSecretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw new JwtAuthorizationException(e);
        }
    }

    public Collection<GrantedAuthority> getAuthorities(String token) {
        String auth = getClaimsFromToken(token).get(CLAIMS_ROLES_KEY).toString();
        return Stream.of(auth.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token).getBody();
    }
}
