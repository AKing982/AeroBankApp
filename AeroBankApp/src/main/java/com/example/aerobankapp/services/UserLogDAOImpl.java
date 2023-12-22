package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.UserLogModel;
import com.example.aerobankapp.repositories.UserLogRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLogDAOImpl implements UserLogDAO
{
    private UserLogRepository userLogRepo;

    @PersistenceContext
    private EntityManager em;

    private AeroLogger aeroLogger = new AeroLogger(UserLogDAOImpl.class);

    @Autowired
    public UserLogDAOImpl(UserLogRepository userLogRepo, EntityManager entityManager)
    {
        this.userLogRepo = userLogRepo;
        this.em = entityManager;
    }

    @Override
    public List<UserLog> findAll()
    {
        aeroLogger.info("UserLog's Found: " + userLogRepo.findAll());
        return userLogRepo.findAll();
    }

    @Override
    @Transactional
    public void save(UserLog obj)
    {
        userLogRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(UserLog obj)
    {
        userLogRepo.delete(obj);
    }

    @Override
    public UserLog findAllById(Long id)
    {
        return userLogRepo.findById((long)id).orElse(null);
    }

    @Override
    public List<UserLog> findByUserName(String user)
    {
        TypedQuery<UserLog> typedQuery = em.createQuery("FROM UserLog WHERE username=:user", UserLog.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(1);
        return typedQuery.getResultList();
    }
}
