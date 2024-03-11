package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountSecurityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSecurityRepository extends JpaRepository<AccountSecurityEntity, Long>
{

}
