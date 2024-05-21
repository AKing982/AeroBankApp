package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.repositories.BillPayeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillPayeesServiceImpl implements BillPayeesService
{
    private final BillPayeesRepository billPayeesRepository;

    @Autowired
    public BillPayeesServiceImpl(BillPayeesRepository billPayeesRepository){
        this.billPayeesRepository = billPayeesRepository;
    }

    @Override
    public List<BillPayeesEntity> findAll() {
        return null;
    }

    @Override
    public void save(BillPayeesEntity obj) {

    }

    @Override
    public void delete(BillPayeesEntity obj) {

    }

    @Override
    public Optional<BillPayeesEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPayeesEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<BillPayeesEntity> findBillPayeesByUserID(int userID) {
        return billPayeesRepository.findBillPayeesEntitiesByUserID(userID);
    }

    @Override
    public List<String> findBillPayeeNamesByUserID(int userID) {
        return billPayeesRepository.findBillPayeeNamesByUserID(userID);
    }
}
