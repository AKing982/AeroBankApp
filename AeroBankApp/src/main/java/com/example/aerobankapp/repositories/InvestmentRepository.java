package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.InvestmentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentAccount, Long>
{

}
