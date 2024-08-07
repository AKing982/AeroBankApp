package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.EmailServerEntity;

import java.util.List;
import java.util.Optional;

public interface EmailServerService
{
    List<EmailServerEntity> findAll();


    void save(EmailServerEntity obj);


    void delete(EmailServerEntity obj);

    void update(EmailServerEntity emailServer);

    Optional<EmailServerEntity> findAllById(Long id);

    List<EmailServerEntity> getEmailServerById(Long id);
    boolean emailServerExists(String host, int port);
}
