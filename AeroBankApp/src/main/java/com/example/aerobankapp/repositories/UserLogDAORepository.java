package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dao.UserLogDao;
import com.example.aerobankapp.entity.UserLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserLogDAORepository implements UserLogDao
{
    private EntityManager entityManager;

    @Autowired
    public UserLogDAORepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(UserLog obj)
    {
        entityManager.persist(obj);
    }

    @Override
    @Transactional
    public void update(UserLog obj)
    {
        entityManager.merge(obj);
    }

    @Override
    @Transactional
    public void delete(UserLog obj)
    {
        entityManager.remove(obj);
    }

    @Override
    public List<UserLog> findAll()
    {
        TypedQuery<UserLog> typedQuery = entityManager.createQuery("FROM UserLog", UserLog.class);
        return typedQuery.getResultList();
    }

    @Override
    public UserLog findById(int id)
    {
        return entityManager.find(UserLog.class, id);
    }
}
