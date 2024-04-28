package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCodeRepository extends JpaRepository<AccountCodeEntity, Long>
{
    @Query(value = "SELECT CONCAT(a.first_initial_segment, CAST(CAST(a.account_type AS UNSIGNED) AS CHAR)) AS shortSegment FROM AccountCodeEntity a WHERE a.account_segment=:account", nativeQuery = true)
    String getAccountCodeShortSegment(@Param("account") int account);
}
