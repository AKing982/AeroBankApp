package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<CheckingAccount, Long>
{

}
