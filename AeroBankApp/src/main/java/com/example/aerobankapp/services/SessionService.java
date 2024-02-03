package com.example.aerobankapp.services;

import com.example.aerobankapp.repositories.SessionRepository;
import com.example.aerobankapp.workbench.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService
{
    @Autowired
    private SessionRepository sessionRepository;

    public void saveSession(Session session)
    {
        sessionRepository.save(session);
    }

    public Session getSession(String sessionID)
    {
        return null;
    }

}
