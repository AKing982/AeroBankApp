package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.PurchaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<PurchaseEntity, Long>
{

}
