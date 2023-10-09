package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Registration;
import com.example.aerobankapp.model.ServiceModel;
import com.example.aerobankapp.repositories.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RegistrationService extends ServiceModel<Registration>
{
    @Override
    List<Registration> findAll();

    @Override
    void save(Registration obj);

    @Override
    void delete(Registration obj);

    @Override
    Registration findAllById(int id);
}
