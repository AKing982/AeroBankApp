package com.example.aerobankapp.workbench.utilities;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class VaultGenerationUtil {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String vaultAddr = "http://127.0.0.1:8200"; // Vault address
        String loginUri = vaultAddr + "/v1/auth/userpass/login/your_username"; // Example path for userpass auth method

        Map<String, String> request = new HashMap<>();
        request.put("password", "your_password");

        Map response = restTemplate.postForObject(loginUri, request, Map.class);

       // System.out.println("Vault Token: " + response.get("auth").get("client_token")); // Extract the token from response
    }

}
