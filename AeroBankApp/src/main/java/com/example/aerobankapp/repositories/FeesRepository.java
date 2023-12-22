package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.FeesEntity;
import com.example.aerobankapp.fees.FeesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeesRepository extends JpaRepository<FeesEntity, Long>
{

}
