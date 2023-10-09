package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Customer;
import com.example.aerobankapp.model.ServiceModel;

import java.util.List;

public interface CustomerService extends ServiceModel<Customer>
{
    @Override
    List<Customer> findAll();

    @Override
    void save(Customer obj);

    @Override
    void delete(Customer obj);

    @Override
    Customer findAllById(int id);
}
