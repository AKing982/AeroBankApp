package com.example.aerobankapp.model;

import java.util.List;
import java.util.Optional;

public interface ServiceDAOModel<T>
{
    List<T> findAll();
    void save(T obj);
    void delete(T obj);
    Optional<T> findAllById(Long id);
    List<T> findByUserName(String user);
}
