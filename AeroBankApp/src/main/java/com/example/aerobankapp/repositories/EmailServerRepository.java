package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.EmailServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailServerRepository extends JpaRepository<EmailServerEntity, Long>
{

}
