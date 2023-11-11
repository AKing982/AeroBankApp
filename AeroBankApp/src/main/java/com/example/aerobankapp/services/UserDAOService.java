package com.example.aerobankapp.services;
;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.User;

import java.util.List;

public interface UserDAOService extends ServiceDAOModel<Users>
{
    @Override
    List<Users> findAll();

    @Override
    void save(Users obj);

    @Override
    void delete(Users obj);

    @Override
    Users findAllById(Long id);

    @Override
    List<Users> findByUserName(String user);




}
