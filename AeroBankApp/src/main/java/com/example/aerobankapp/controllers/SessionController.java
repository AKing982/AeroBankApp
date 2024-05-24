package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/session", method=RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
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
