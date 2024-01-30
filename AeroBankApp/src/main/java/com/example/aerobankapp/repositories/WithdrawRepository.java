package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.WithdrawEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawRepository extends CrudRepository<WithdrawEntity, Long>
{

}
