package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.repositories.BillPaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BillPaymentHistoryServiceImpl implements BillPaymentHistoryService
{
    private final BillPaymentHistoryRepository billPaymentHistoryRepository;

    @Autowired
    public BillPaymentHistoryServiceImpl(BillPaymentHistoryRepository billPaymentHistoryRepository){
        this.billPaymentHistoryRepository = billPaymentHistoryRepository;
    }

    @Override
    public List<BillPaymentHistoryEntity> findAll() {
        return null;
    }

    @Override
    @Transactional
    public void save(BillPaymentHistoryEntity obj) {
        billPaymentHistoryRepository.save(obj);
    }

    @Override
    public void delete(BillPaymentHistoryEntity obj) {

    }

    @Override
    public Optional<BillPaymentHistoryEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPaymentHistoryEntity> findByUserName(String user) {
        return null;
    }
}
