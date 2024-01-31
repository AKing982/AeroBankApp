package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.ConnectionsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<ConnectionsEntity, Long>
{

}
