package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.UserLogModel;
import com.example.aerobankapp.repositories.UserLogRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService
{
    @Autowired
    private UserLogRepository userLogRepo;
    private AeroLogger aeroLogger = new AeroLogger(UserLogServiceImpl.class);




    @Override
    public List<UserLogModel> findAll()
    {
        aeroLogger.info("UserLog's Found: " + userLogRepo.findAll());
        return userLogRepo.findAll();
    }

    @Override
    @Transactional
    public void save(UserLogModel obj)
    {
        userLogRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(UserLogModel obj)
    {
        userLogRepo.delete(obj);
    }

    @Override
    public UserLogModel findAllById(int id)
    {
        return userLogRepo.findById((long)id).orElse(null);
    }
}
