package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
