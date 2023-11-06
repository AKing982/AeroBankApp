package com.example.aerobankapp.repositories;

import com.example.aerobankapp.model.UserLogModel;
import com.example.aerobankapp.services.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserLogRepositoryImpl implements UserLogService
{
    @Autowired
    private UserLogRepository userLogRepo;


    @Override
    public List<UserLogModel> findAll() {
        return null;
    }

    @Override
    public void save(UserLogModel obj) {

    }

    @Override
    public void delete(UserLogModel obj) {

    }

    @Override
    public UserLogModel findAllById(int id) {
        return null;
    }
}
