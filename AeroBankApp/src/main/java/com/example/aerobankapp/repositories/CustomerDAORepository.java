package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dao.CustomerDao;
import com.example.aerobankapp.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomerDAORepository implements CustomerDao
{
    private EntityManager entityManager;

    @Autowired
    public CustomerDAORepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Customer obj)
    {
        entityManager.persist(obj);
    }

    @Override
    @Transactional
    public void update(Customer obj)
    {
        entityManager.merge(obj);
    }

    @Override
    @Transactional
    public void delete(Customer obj)
    {
        entityManager.remove(obj);
    }

    @Override
    public List<Customer> findAll()
    {
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery("FROM Customer", Customer.class);

        return customerTypedQuery.getResultList();
    }

    @Override
    public Customer findById(int id)
    {
       return entityManager.find(Customer.class, id);
    }
}
