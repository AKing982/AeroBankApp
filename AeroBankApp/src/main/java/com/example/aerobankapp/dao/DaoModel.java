package com.example.aerobankapp.dao;

import java.util.List;

public interface DaoModel<T>
{
    void save(T obj);

    void update(T obj);

    void delete(T obj);

    List<T> findAll();

    T findById(int id);
}
