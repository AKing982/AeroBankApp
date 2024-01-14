package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.repositories.SavingsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SavingsAccountDAOImpl implements SavingsAccountDAO
{
    private EntityManager entityManager;
    private SavingsRepository savingsRepository;

    @Autowired
    public SavingsAccountDAOImpl(SavingsRepository savingsRepository, EntityManager entityManager)
    {
        this.savingsRepository = savingsRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<SavingsAccountEntity> findAll()
    {
        return savingsRepository.findAll();
    }

    @Override
    public void save(SavingsAccountEntity obj)
    {
        savingsRepository.save(obj);
    }

    @Override
    public void delete(SavingsAccountEntity obj)
    {
        savingsRepository.delete(obj);
    }

    @Override
    public Optional<SavingsAccountEntity> findAllById(Long id)
    {
        return savingsRepository.findById(id);
    }

    @Override
    public List<SavingsAccountEntity> findByUserName(String user)
    {

        TypedQuery<SavingsAccountEntity> typedQuery = entityManager.createQuery("FROM SavingsAccountEntity WHERE user=:user", SavingsAccountEntity.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }
}
