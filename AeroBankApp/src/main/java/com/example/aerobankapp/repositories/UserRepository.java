package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
