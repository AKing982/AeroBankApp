package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dao.RegistrationDao;
import com.example.aerobankapp.entity.Registration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long>
{

}
