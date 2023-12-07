package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLogEntity;
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
public class UserLogServiceImpl implements UserLogService
{
    private UserLogRepository userLogRepo;

    @PersistenceContext
    private EntityManager em;

    private AeroLogger aeroLogger = new AeroLogger(UserLogServiceImpl.class);

    @Autowired
    public UserLogServiceImpl(UserLogRepository userLogRepo, EntityManager entityManager)
    {
        this.userLogRepo = userLogRepo;
        this.em = entityManager;
    }

    @Override
    public List<UserLogEntity> findAll()
    {
        aeroLogger.info("UserLog's Found: " + userLogRepo.findAll());
        return userLogRepo.findAll();
    }

    @Override
    @Transactional
    public void save(UserLogEntity obj)
    {
        userLogRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(UserLogEntity obj)
    {
        userLogRepo.delete(obj);
    }

    @Override
    public UserLogEntity findAllById(Long id)
    {
        return userLogRepo.findById((long)id).orElse(null);
    }

    @Override
    public List<UserLogEntity> findByUserName(String user)
    {
        TypedQuery<UserLogEntity> typedQuery = em.createQuery("FROM UserLogEntity WHERE username=:user", UserLogEntity.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(1);
        return typedQuery.getResultList();
    }
}
