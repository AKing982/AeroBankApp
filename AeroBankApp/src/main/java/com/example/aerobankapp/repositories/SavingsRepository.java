package com.example.aerobankapp.repositories;

import com.example.aerobankapp.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<SavingsAccount, Long>
{

}
