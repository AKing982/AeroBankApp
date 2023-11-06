package com.example.aerobankapp.repositories;

import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.UserDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
    public User findAllById(int id) {
        return userRepository.findAllById((long)id).;
    }
}
