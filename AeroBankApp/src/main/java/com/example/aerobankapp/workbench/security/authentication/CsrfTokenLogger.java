package com.example.aerobankapp.workbench.security.authentication;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;


public class CsrfTokenLogger implements Filter
{
    private Logger logger = LoggerFactory.getLogger(CsrfTokenLogger.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        CsrfToken o = (CsrfToken) servletRequest.getAttribute("_csrf");

        logger.info("CSRF token " + o.getToken());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
