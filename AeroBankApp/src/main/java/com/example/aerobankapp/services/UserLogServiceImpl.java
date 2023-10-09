package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.repositories.UserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService
{
    private final UserLogRepository userLogRepo;

    @Autowired
    public UserLogServiceImpl(UserLogRepository userLogRepo)
    {
        this.userLogRepo = userLogRepo;
    }


    @Override
    public List<UserLog> findAll()
    {
        return userLogRepo.findAll();
    }

    @Override
    @Transactional
    public void save(UserLog obj)
    {
        userLogRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(UserLog obj)
    {
        userLogRepo.delete(obj);
    }

    @Override
    public UserLog findAllById(int id)
    {
        return userLogRepo.findById((long)id).orElse(null);
    }
}
