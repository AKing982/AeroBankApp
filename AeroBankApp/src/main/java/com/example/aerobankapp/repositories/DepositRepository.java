package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.DepositsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends CrudRepository<DepositsEntity, Long>
{

}
