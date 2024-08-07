package com.example.aerobankapp.workbench.model;

import com.example.aerobankapp.workbench.security.authentication.SecurityConfig;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Setter
@Getter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class LoginModel
{
    private String username;
    private String password;
    private String encodedPassword;
    private boolean isEncoded;
    private Map<String, String> credentialsMap = new HashMap<>();


    public LoginModel(String user, String pass)
    {
        this.username = user;
        this.password = pass;
        setCredentialsMap(user, pass);
    }

    public Map<String, String> getEncodedCredentialsMap()
    {
        if(isEncoded)
        {
            setCredentialsMap(username, getEncodedPassword());
        }
        return credentialsMap;
    }


    public boolean isEncoded()
    {
        this.isEncoded = !Objects.equals(encodedPassword, password);
        return isEncoded;
    }

    private void setCredentialsMap(final String user, final String pass)
    {
        credentialsMap.put(user, pass);
    }



    public String getPasswordFromMap()
    {
        return getEncodedCredentialsMap().entrySet().stream()
                .filter(entry -> username.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }


}
