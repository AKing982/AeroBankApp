package com.example.aerobankapp.services;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface CustomQueryService<T>
{
    Optional<T> executeQuery(String jpql, Object... params);

    List<T> executeQueryForList(String jpql, Object... params);

    void setEntityManager(EntityManager em);
}
