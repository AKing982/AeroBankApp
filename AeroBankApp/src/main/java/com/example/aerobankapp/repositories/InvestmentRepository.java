package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.InvestmentAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface InvestmentRepository extends JpaRepository<InvestmentAccountEntity, Long>
{

}
