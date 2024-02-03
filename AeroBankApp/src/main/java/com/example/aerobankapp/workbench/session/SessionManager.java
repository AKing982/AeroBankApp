package com.example.aerobankapp.workbench.session;

import com.example.aerobankapp.workbench.utilities.User;


public interface SessionManager
{
    Session createSession(User user);
    Session getSession(String sessionID);
    boolean validateSession(String sessionID);
    void renewSession(Session session);
    void terminateSession(Session session);
    boolean isSessionActive(Session session);
    void handleTimeout(Session session);
}
