package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.InvestmentAccountEntity;
import com.example.aerobankapp.repositories.InvestmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Deprecated
public class InvestmentAccountDAOImpl implements InvestmentAccountDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private InvestmentRepository investmentRepository;

    @Autowired
    public InvestmentAccountDAOImpl(EntityManager entityManager, InvestmentRepository investmentRepository)
    {
        this.entityManager = entityManager;
        this.investmentRepository = investmentRepository;
    }

    @Override
    public List<InvestmentAccountEntity> findAll() {
        return null;
    }

    @Override
    public void save(InvestmentAccountEntity obj) {

    }

    @Override
    public void delete(InvestmentAccountEntity obj) {

    }

    @Override
    public Optional<InvestmentAccountEntity> findAllById(Long id) {
        return null;
    }

    @Override
    public List<InvestmentAccountEntity> findByUserName(String user) {
        return null;
    }
}
