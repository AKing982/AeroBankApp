package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/csrf", method=RequestMethod.GET)
public class CsrfController
{
    private final Logger logger = LoggerFactory.getLogger(CsrfController.class);

    @GetMapping(value ="/token")
    public CsrfToken csrfToken(HttpServletRequest request)
    {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        HttpSession session = request.getSession(false);
        if(session != null)
        {
            logger.info("Session ID: " + session.getId());
            logger.info("CSRF Token: " + csrf.getToken());
        }
        return csrf;
    }
}
