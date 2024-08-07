package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/preferences")
@CrossOrigin(value="http://localhost:3000")
public class SessionPreferencesController {

    @GetMapping("/set")
    public String setPreferences(HttpSession session, @RequestParam String key, @RequestParam String value){
        session.setAttribute(key, value);
        return "Preference set: " + key + " = " + value;
    }

    @GetMapping("/get")
    public String getPreferences(HttpSession session, @RequestParam String key){
        String value = (String)session.getAttribute(key);
        return "Preference: " + key + " = " + value;
    }



}
