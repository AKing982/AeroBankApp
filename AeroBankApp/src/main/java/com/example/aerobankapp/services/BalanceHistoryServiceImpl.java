package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BalanceHistory;
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
public class BalanceHistoryServiceImpl implements ServiceDAOModel<BalanceHistory>
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
    public List<BalanceHistory> findAll()
    {
        return balanceHistRepo.findAll();
    }

    @Override
    public void save(BalanceHistory obj)
    {
        balanceHistRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(BalanceHistory obj)
    {
        balanceHistRepo.delete(obj);
    }

    @Override
    @Transactional
    public BalanceHistory findAllById(Long id)
    {
        return balanceHistRepo.findById(id).orElse(null);
    }

    @Override
    public List<BalanceHistory> findByUserName(String user)
    {
        TypedQuery<BalanceHistory> query = entityManager.createQuery("FROM BalanceHistory WHERE user=:user", BalanceHistory.class);
        query.setParameter("user", user);
        query.setMaxResults(20);
        return query.getResultList();
    }
}
