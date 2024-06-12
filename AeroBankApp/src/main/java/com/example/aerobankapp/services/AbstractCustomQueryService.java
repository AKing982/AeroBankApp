package com.example.aerobankapp.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCustomQueryService<T> implements CustomQueryService<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Optional<T> executeQuery(String jpql, Object... params) {
        Query query = entityManager.createQuery(jpql);
        for(int i = 0; i < params.length; i++){
            query.setParameter(i + 1, params[i]);
        }
        T result;
        try{
            result = (T) query.getSingleResult();
        }catch(Exception e){
            result = null;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<T> executeQueryForList(String jpql, Object... params) {
        Query query = entityManager.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }
}
