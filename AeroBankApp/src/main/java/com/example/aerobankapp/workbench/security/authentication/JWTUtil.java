package com.example.aerobankapp.workbench.security.authentication;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class JWTUtil
{
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.ES256);
    private static final long EXPIRATION_TIME = 36000000;

    public static String generateToken(String username, List<String> roles)
    {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject()
    }
}
