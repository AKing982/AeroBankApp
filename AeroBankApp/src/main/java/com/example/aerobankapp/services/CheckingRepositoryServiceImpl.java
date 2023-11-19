package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.repositories.CheckingRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckingRepositoryServiceImpl implements CheckingRepositoryService
{
    private CheckingRepository checkingRepo;

    private EntityManager entityManager;

    @Autowired
    public CheckingRepositoryServiceImpl(CheckingRepository checkingRepository, EntityManager entityManager)
    {
        this.checkingRepo = checkingRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<CheckingAccount> findAll()
    {
        return checkingRepo.findAll();
    }

    @Override
    @Transactional
    public void save(CheckingAccount obj)
    {
        checkingRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(CheckingAccount obj)
    {
        checkingRepo.delete(obj);
    }

    @Override
    public CheckingAccount findAllById(Long id)
    {
        return checkingRepo.findById(id).orElse(null);
    }

    @Override
    public List<CheckingAccount> findByUserName(String user)
    {
        TypedQuery<CheckingAccount> checkingAccountTypedQuery = entityManager.createQuery("FROM CheckingAccount WHERE userName=:user", CheckingAccount.class);
        checkingAccountTypedQuery.setParameter("user", user);
        checkingAccountTypedQuery.setMaxResults(10);
        return checkingAccountTypedQuery.getResultList();
    }
}
