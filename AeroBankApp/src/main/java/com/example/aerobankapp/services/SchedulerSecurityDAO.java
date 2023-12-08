package com.example.aerobankapp.services;

import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.repositories.SchedulerSecurityRepository;
import com.example.aerobankapp.entity.SchedulerSecurityEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerSecurityDAO implements ServiceDAOModel<SchedulerSecurityEntity>
{
    private EntityManager entityManager;
    private SchedulerSecurityRepository schedulerSecurityRepository;

    @Autowired
    public SchedulerSecurityDAO(SchedulerSecurityRepository schedulerSecurityRepository, EntityManager entityManager)
    {
        this.schedulerSecurityRepository = schedulerSecurityRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<SchedulerSecurityEntity> findAll()
    {
        return schedulerSecurityRepository.findAll();
    }

    @Override
    public void save(SchedulerSecurityEntity obj)
    {
        schedulerSecurityRepository.save(obj);
    }

    @Override
    public void delete(SchedulerSecurityEntity obj)
    {
        schedulerSecurityRepository.delete(obj);
    }

    @Override
    public SchedulerSecurityEntity findAllById(Long id)
    {
        return schedulerSecurityRepository.findById(id).orElse(null);
    }

    @Override
    public List<SchedulerSecurityEntity> findByUserName(String user)
    {
        TypedQuery<SchedulerSecurityEntity> typedQuery = entityManager.createQuery("FROM SchedulerSecurityEntity WHERE user=:user", SchedulerSecurityEntity.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }
}
