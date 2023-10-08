package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dao.RegistrationDao;
import com.example.aerobankapp.entity.Registration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegistrationDAORepository implements RegistrationDao
{
    private EntityManager entityManager;

    @Autowired
    public RegistrationDAORepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Registration obj)
    {
        entityManager.persist(obj);
    }

    @Override
    public void update(Registration obj)
    {
        entityManager.merge(obj);
    }

    @Override
    public void delete(Registration obj)
    {
        entityManager.remove(obj);
    }

    @Override
    public List<Registration> findAll()
    {
        TypedQuery<Registration> typedQuery = entityManager.createQuery("FROM Registration", Registration.class);
        return typedQuery.getResultList();
    }

    @Override
    public Registration findById(int id)
    {
        return entityManager.find(Registration.class, id);
    }
}
