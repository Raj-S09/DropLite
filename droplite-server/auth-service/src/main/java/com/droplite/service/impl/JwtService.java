package com.droplite.service.impl;

import com.droplite.constant.AuthConstants;
import com.droplite.dto.UserDto;
import com.droplite.service.IJwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService implements IJwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expire-in}")
    private Long expireIn;

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim(AuthConstants.KEY_CLAIMS_AUTHORITY, role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireIn))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private Key getSigningKey() {
        byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean validateToken(String token, UserDto userDto) {
        return extractUsername(token).equals(userDto.getUsername());
    }
}
