package com.example.aerobankapp.workbench.model;

import com.example.aerobankapp.workbench.security.authentication.SecurityConfig;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel
{
    private String username;
    private String password;
    private String encodedPassword;
    private Map<String, String> credentialsMap = new HashMap<>();

    @Autowired
    private SecurityConfig securityConfig = new SecurityConfig();

    public LoginModel(String user, String pass)
    {
        this.username = user;
        this.password = pass;
    }

    public String getEncodedPassword()
    {
        return encode(password);
    }

    public Map<String, String> getEncodedCredentialsMap()
    {
        this.encodedPassword = encode(password);
        setCredentialsMap(username, encodedPassword);
        return credentialsMap;
    }

    private void setCredentialsMap(final String user, final String pass)
    {
        credentialsMap.put(user, pass);
    }

    private String encode(final String param)
    {
        return securityConfig.passwordEncoder().encode(param);
    }


}
