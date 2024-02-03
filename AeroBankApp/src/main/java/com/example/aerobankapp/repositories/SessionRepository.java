package com.example.aerobankapp.repositories;

import com.example.aerobankapp.workbench.session.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>
{

}
