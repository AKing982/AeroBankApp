package com.example.aerobankapp.services;

import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.repositories.DepositRepository;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class DepositServiceImpl implements DepositService
{
    private final DepositRepository depositRepository;
    private final DepositEngine depositEngine;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository,
                              DepositEngine depositEngine,
                              EntityManager entityManager)
    {
        this.depositRepository = depositRepository;
        this.entityManager = entityManager;
        this.depositEngine = depositEngine;
    }

    @Override
    public List<DepositsEntity> findAll() {
        return null;
    }

    @Override
    public void save(DepositsEntity obj) {

    }

    @Override
    public void delete(DepositsEntity obj) {

    }

    @Override
    public Optional<DepositsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DepositsEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByUserNameDesc(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByAcctID(int acctID) {
        TypedQuery<DepositsEntity> depositsEntityTypedQuery = getEntityManager()
                .createQuery("SELECT d FROM DepositsEntity d JOIN d.account a WHERE a.acctID = :acctID", DepositsEntity.class);
        depositsEntityTypedQuery.setParameter("acctID", acctID);
        depositsEntityTypedQuery.setMaxResults(30);
        return depositsEntityTypedQuery.getResultList();
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserIDASC(Long id) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserID_DESC(Long id) {
        return null;
    }

    @Override
    public void submit(DepositRequest request)
    {
        System.out.println("Deposit has been recieved");
    }
}
