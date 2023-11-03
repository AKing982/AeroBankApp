package com.example.aerobankapp.services;
;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.User;

import java.util.List;

public interface UserDAOService extends ServiceDAOModel<User>
{
    @Override
    List<User> findAll();

    @Override
    void save(User obj);

    @Override
    void delete(User obj);

    @Override
    User findAllById(int id);
}
