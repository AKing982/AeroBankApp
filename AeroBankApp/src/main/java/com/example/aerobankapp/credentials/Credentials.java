package com.example.aerobankapp.credentials;

import java.util.HashMap;
import java.util.Map;

public class Credentials<String> implements Map.Entry<String, String>
{
    private Map<java.lang.String, java.lang.String> credentialsMap = new HashMap<>();
    private java.lang.String userKey;
    private java.lang.String password;
    public Credentials(java.lang.String user, java.lang.String pass)
    {
        this.userKey = user;
        this.password = pass;
    }

    @Override
    public String getKey()
    {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String setValue(String value) {
        return null;
    }
}
