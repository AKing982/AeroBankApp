package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SavingsAccount;
import com.example.aerobankapp.repositories.SavingsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

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
    public List<SavingsAccount> findAll()
    {
        return savingsRepository.findAll();
    }

    @Override
    public void save(SavingsAccount obj)
    {
        savingsRepository.save(obj);
    }

    @Override
    public void delete(SavingsAccount obj)
    {
        savingsRepository.delete(obj);
    }

    @Override
    public SavingsAccount findAllById(Long id)
    {
        return savingsRepository.findById(id).orElse(null);
    }

    @Override
    public List<SavingsAccount> findByUserName(String user)
    {

        TypedQuery<SavingsAccount> typedQuery = entityManager.createQuery("FROM SavingsAccount WHERE user=:user", SavingsAccount.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }
}
