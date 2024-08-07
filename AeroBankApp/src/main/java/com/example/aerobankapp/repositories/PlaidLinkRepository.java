package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.PlaidLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PlaidLinkRepository extends JpaRepository<PlaidLinkEntity, Long>
{
    @Query("SELECT e FROM PlaidLinkEntity e WHERE e.institution_name=:name")
    Collection<PlaidLinkEntity> findAllByInstitutionName(@Param("name") String institutionName);

    @Query("SELECT e FROM PlaidLinkEntity e WHERE e.user.userID=:id")
    Optional<PlaidLinkEntity> findByUserId(@Param("id") int userId);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM PlaidLinkEntity u WHERE u.user.userID=:id) THEN true ELSE false END")
    Boolean existsByUserId(@Param("id") int userId);

}