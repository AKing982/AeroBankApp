package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransferEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<TransferEntity, Long>
{

}
