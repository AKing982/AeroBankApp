package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.Customer;

import java.util.List;

public interface CustomerDao extends DaoModel<Customer>
{
    @Override
    void save(Customer obj);

    @Override
    void update(Customer obj);

    @Override
    void delete(Customer obj);

    @Override
    List<Customer> findAll();

    @Override
    Customer findById(int id);
}
