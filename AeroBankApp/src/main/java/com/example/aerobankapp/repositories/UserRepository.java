package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findById(Long id);
}
