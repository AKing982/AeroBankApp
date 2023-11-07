package com.example.aerobankapp.repositories;

import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.UserDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserDAOService
{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User obj) {
        userRepository.save(obj);
    }

    @Override
    public void delete(User obj) {
        userRepository.delete(obj);
    }

    @Override
    public User findAllById(int id)
    {
        return userRepository.findAllById((long)id).;
    }
}
