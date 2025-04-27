package com.master.project.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtValidator.class);

    @Value("${jwt.secret}")
    private String secretKey;

    public boolean validateToken(String token) {
        try {
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                    SignatureAlgorithm.HS256.getJcaName());

            Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            LOGGER.error("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
