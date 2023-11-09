package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SavingsAccount;
import com.example.aerobankapp.repositories.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService
{
    private SavingsRepository savingsRepository;

    @Autowired
    public SavingsAccountServiceImpl(SavingsRepository savingsRepository)
    {
        this.savingsRepository = savingsRepository;
    }

    @Override
    public List<SavingsAccount> findAll() {
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
    public SavingsAccount findAllById(int id) {
        return null;
    }

    @Override
    public List<SavingsAccount> findByUserName(String user) {
        return null;
    }
}
