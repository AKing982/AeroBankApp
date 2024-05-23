package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/set")
    public String setSession(HttpSession session, @RequestParam String name, @RequestParam String roles, @RequestParam String token){
        session.setAttribute("username", name);
        session.setAttribute("roles", roles);
        session.setAttribute("toke", token);
        return "Session attributes set: username = " + name + ", roles = " + roles + ", token = " + token;
    }

    @GetMapping("/get")
    public String getSession(HttpSession session){
        String username = (String)session.getAttribute("username");
        String roles = (String) session.getAttribute("roles");
        String token = (String) session.getAttribute("token");
        return "Session attributes: username = " + username + ", roles = " + roles + ", token = " + token;
    }
}
