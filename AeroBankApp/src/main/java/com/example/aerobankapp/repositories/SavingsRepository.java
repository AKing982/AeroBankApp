package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.SavingsAccount;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

@Repository
public interface SavingsRepository extends JpaRepository<SavingsAccount, Long>
{

}
