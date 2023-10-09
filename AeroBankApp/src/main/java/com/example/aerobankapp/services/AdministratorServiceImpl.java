package com.example.aerobankapp.services;


import com.example.aerobankapp.repositories.AdministratorRepository;
import com.example.aerobankapp.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdministratorServiceImpl implements AdministratorService
{
    private final AdministratorRepository administratorRepo;

    @Autowired
    public AdministratorServiceImpl(AdministratorRepository administratorRepo)
    {
        this.administratorRepo = administratorRepo;
    }

    @Override
    public List<Administrator> findAll() {
        return administratorRepo.findAll();
    }


    @Override
    public Administrator findAllById(int id) {
        return administratorRepo.findById((long) id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Administrator obj)
    {
        administratorRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(Administrator obj)
    {
        administratorRepo.delete(obj);
    }

}
