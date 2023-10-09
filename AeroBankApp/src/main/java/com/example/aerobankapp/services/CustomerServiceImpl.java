package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Customer;
import com.example.aerobankapp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService
{
    private final CustomerRepository customerRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepo)
    {
        this.customerRepo = customerRepo;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    @Transactional
    public void save(Customer obj)
    {
        customerRepo.save(obj);
    }

    @Override
    @Transactional
    public void delete(Customer obj)
    {
        customerRepo.delete(obj);
    }

    @Override
    public Customer findAllById(int id) {
        return customerRepo.findById((long)id).orElse(null);
    }
}
