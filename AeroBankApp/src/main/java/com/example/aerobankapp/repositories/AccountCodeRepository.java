package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCodeRepository extends JpaRepository<AccountCodeEntity, Long>
{

}
