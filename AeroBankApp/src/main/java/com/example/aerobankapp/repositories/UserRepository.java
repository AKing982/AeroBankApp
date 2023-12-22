package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{

}
