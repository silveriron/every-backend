package com.every.everybackend.common.adapter;

import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.AuthErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtAdapterImp implements JwtAdapter{

    @Value("${jwt.secret}")
    private String key;
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    private SecretKey secretKey() {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String createToken(String email) {
        Instant instant = LocalDateTime.now().plusMinutes(expirationTime).atZone(ZoneId.systemDefault()).toInstant();

        Date expiration = Date.from(instant);

        return Jwts.builder()
                .subject(email)
                .expiration(expiration)
                .signWith(secretKey())
                .compact();
    }

    @Override
    public String getEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(token).getPayload().getSubject();

        } catch (Exception e) {
            throw new ApiException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
