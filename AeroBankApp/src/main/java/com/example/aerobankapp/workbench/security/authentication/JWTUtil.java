package com.example.aerobankapp.workbench.security.authentication;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JWTUtil {
    private static final String SECRET_KEY = "a2n4m52i8";
    private static final long EXPIRATION_TIME = 36000000;


    public String generateToken(Authentication authentication)
    {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

//        return Jwts.builder()
//                .claim("authorities", authorities)
//                .compact();
        return null;
    }

    public String generateToken(String username)
    {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

      return null;
    }



}
