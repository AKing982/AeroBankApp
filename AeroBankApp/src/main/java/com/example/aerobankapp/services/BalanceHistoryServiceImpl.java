package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.repositories.BalanceHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BalanceHistoryServiceImpl implements BalanceHistoryService
{
    private BalanceHistoryRepository balanceHistRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BalanceHistoryServiceImpl(BalanceHistoryRepository balanceHistRepo, EntityManager entityManager)
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
    public Optional<BalanceHistoryEntity> findAllById(Long id)
    {
        return balanceHistRepo.findById(id);
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
