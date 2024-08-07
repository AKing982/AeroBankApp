package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface CheckingRepository extends JpaRepository<CheckingAccountEntity, Long>
{

}
