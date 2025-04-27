package com.master.project.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Getter
@Setter
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
        public String generateToken(String subject) {
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                    SignatureAlgorithm.HS256.getJcaName());
            return Jwts.builder()
                    .setSubject(subject)
                    .signWith(hmacKey)
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .compact();
        }

}
