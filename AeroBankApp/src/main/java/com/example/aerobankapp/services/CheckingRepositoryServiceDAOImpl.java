package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.repositories.CheckingRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Deprecated
public class CheckingRepositoryServiceDAOImpl implements CheckingAccountDAO
{
    private CheckingRepository checkingRepo;
    private EntityManager entityManager;

    @Autowired
    public CheckingRepositoryServiceDAOImpl(CheckingRepository checkingRepository, EntityManager entityManager)
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
    public Optional<CheckingAccountEntity> findAllById(Long id)
    {
        return checkingRepo.findById(id);
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
