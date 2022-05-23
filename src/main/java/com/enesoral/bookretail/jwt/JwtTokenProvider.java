package com.enesoral.bookretail.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static java.util.Date.from;

@Component
public class JwtTokenProvider {

    private final Long expiryInMs;

    private final String secretKey;

    private final String encodedSecretKey;

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.expiryInMs:3600000}") Long expiryInMs,
                            @Value("${jwt.secretKey:Qgvi+7.q[gv*01$XQ5cR@*l2;?A7Clx6$-f1:WZL}") String secretKey,
                            UserDetailsService userDetailsService) {
        this.expiryInMs = expiryInMs;
        this.secretKey = secretKey;
        this.userDetailsService = userDetailsService;
        encodedSecretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    public String createToken(String email, List<String> roles) {

        final Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + expiryInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenWithUserName(String username) {
        final Date validity = new Date(new Date().getTime() + expiryInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        final JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        final String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        final JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
        try {
            final Jws<Claims> claims = parser.parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.encodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}