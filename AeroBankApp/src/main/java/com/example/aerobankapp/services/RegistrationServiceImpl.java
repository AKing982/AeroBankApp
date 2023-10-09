package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Registration;
import com.example.aerobankapp.repositories.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService
{
    private final RegistrationRepository registrationRepo;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepo)
    {
        this.registrationRepo = registrationRepo;
    }

    @Override
    public List<Registration> findAll() {
        return registrationRepo.findAll();
    }

    @Override
    public void save(Registration obj)
    {
        registrationRepo.save(obj);
    }

    @Override
    public void delete(Registration obj)
    {
        registrationRepo.delete(obj);
    }

    @Override
    public Registration findAllById(int id)
    {
        return registrationRepo.findById((long)id).orElse(null);
    }
}
