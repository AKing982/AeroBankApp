package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dao.AdministratorDao;
import com.example.aerobankapp.entity.Administrator;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AdministratorDAORepository implements AdministratorDao
{
    private EntityManager entityManager;

    @Autowired
    public AdministratorDAORepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Administrator obj)
    {
        entityManager.persist(obj);
    }

    @Override
    @Transactional
    public void update(Administrator obj)
    {
        entityManager.merge(obj);
    }

    @Override
    @Transactional
    public void delete(Administrator obj)
    {
        entityManager.remove(obj);
    }

    @Override
    public List<Administrator> findAll()
    {
        TypedQuery<Administrator> typedQuery = entityManager.createQuery("FROM Administrator", Administrator.class);

        return typedQuery.getResultList();
    }

    @Override
    public Administrator findById(int id)
    {
        return entityManager.find(Administrator.class, id);
    }
}
