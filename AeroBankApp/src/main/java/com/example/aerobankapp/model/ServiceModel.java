package com.example.aerobankapp.model;

import java.util.List;

public interface ServiceModel<T>
{
    List<T> findAll();
    void save(T obj);
    void delete(T obj);

    T findAllById(int id);
}
