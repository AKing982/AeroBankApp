package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountDetailsEntity;
import jdk.jfr.Registered;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends CrudRepository<AccountDetailsEntity, Long>
{

}
