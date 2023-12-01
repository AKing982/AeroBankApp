package com.example.aerobankapp.workbench.security.authentication;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTUtil
{
    private static final String SECRET_KEY = "a2n4m52i8";
    private static final long EXPIRATION_TIME = 36000000;

    public String generateToken(String username)
    {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.ES256, SECRET_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try
        {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;

        } catch (Exception e)
        {
            return false;
        }
    }


}