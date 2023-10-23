package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDAOService
{
    private UserRepository userRepository;
    private AeroLogger aeroLogger = new AeroLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(Users obj) {
        userRepository.save(obj);
    }

    @Override
    public void delete(Users obj)
    {
        userRepository.delete(obj);
    }

    @Override
    public Users findAllById(int id)
    {
        return userRepository.findById((long)id).orElse(null);
    }
}
