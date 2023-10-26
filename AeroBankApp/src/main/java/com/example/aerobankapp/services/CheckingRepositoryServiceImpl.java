package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.repositories.CheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckingRepositoryServiceImpl implements CheckingRepositoryService
{
    private final CheckingRepository checkingRepo;

    @Autowired
    public CheckingRepositoryServiceImpl(CheckingRepository checkingRepository)
    {
        this.checkingRepo = checkingRepository;
    }

    @Override
    @Transactional
    public List<CheckingAccount> findAll() {
        List<CheckingAccount> checkingAccounts = checkingRepo.findAll();
        return checkingAccounts;
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
    public CheckingAccount findAllById(int id)
    {
        return checkingRepo.findById((long)id).orElse(null);
    }
}
