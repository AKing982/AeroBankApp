package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.ExternalAccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAccountsRepository extends JpaRepository<ExternalAccountsEntity, Long>
{

}
