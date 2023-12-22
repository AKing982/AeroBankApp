package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.repositories.BalanceHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BalanceHistoryDAOImpl implements BalanceHistoryDAO
{
    private BalanceHistoryRepository balanceHistRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BalanceHistoryDAOImpl(BalanceHistoryRepository balanceHistRepo, EntityManager entityManager)
    {
        this.balanceHistRepo = balanceHistRepo;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<BalanceHistoryEntity> findAll()
    {
        return balanceHistRepo.findAll();
    }

    @Override
    public void save(BalanceHistoryEntity obj)
    {
        balanceHistRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(BalanceHistoryEntity obj)
    {
        balanceHistRepo.delete(obj);
    }

    @Override
    @Transactional
    public BalanceHistoryEntity findAllById(Long id)
    {
        return balanceHistRepo.findById(id).orElse(null);
    }

    @Override
    public List<BalanceHistoryEntity> findByUserName(String user)
    {
        TypedQuery<BalanceHistoryEntity> query = entityManager.createQuery("FROM BalanceHistoryEntity WHERE user=:user", BalanceHistoryEntity.class);
        query.setParameter("user", user);
        query.setMaxResults(20);
        return query.getResultList();
    }
}
