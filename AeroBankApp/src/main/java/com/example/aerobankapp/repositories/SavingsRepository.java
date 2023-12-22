package com.example.aerobankapp.repositories;



import com.example.aerobankapp.entity.SavingsAccountEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<SavingsAccountEntity, Long>
{

}
