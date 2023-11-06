package com.example.aerobankapp.repositories;


import com.example.aerobankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUserName(String user);
    List<User> findByLastName(String last);
}
