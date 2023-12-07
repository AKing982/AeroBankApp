package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccountEntity;
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
    public List<CheckingAccountEntity> findAll()
    {
        return checkingRepo.findAll();
    }

    @Override
    @Transactional
    public void save(CheckingAccountEntity obj)
    {
        checkingRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(CheckingAccountEntity obj)
    {
        checkingRepo.delete(obj);
    }

    @Override
    public CheckingAccountEntity findAllById(Long id)
    {
        return checkingRepo.findById(id).orElse(null);
    }

    @Override
    public List<CheckingAccountEntity> findByUserName(String user)
    {
        TypedQuery<CheckingAccountEntity> checkingAccountTypedQuery = entityManager.createQuery("FROM CheckingAccountEntity WHERE userName=:user", CheckingAccountEntity.class);
        checkingAccountTypedQuery.setParameter("user", user);
        checkingAccountTypedQuery.setMaxResults(10);
        return checkingAccountTypedQuery.getResultList();
    }
}
