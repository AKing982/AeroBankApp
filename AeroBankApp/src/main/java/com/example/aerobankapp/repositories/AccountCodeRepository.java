package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCodeRepository extends JpaRepository<AccountCodeEntity, Long>
{
    @Query(value = "SELECT CONCAT(a.first_initial_segment, CAST(CAST(a.account_type AS UNSIGNED) AS CHAR)) AS shortSegment FROM AccountCodeEntity a WHERE a.account_segment=:account", nativeQuery = true)
    String getAccountCodeShortSegment(@Param("account") int account);

    @Query("SELECT ac FROM AccountCodeEntity ac JOIN ac.user u WHERE u.userID=:userID")
    List<AccountCodeEntity> findAccountCodeEntitiesByUserID(@Param("userID") int userID);

    @Query("SELECT ac FROM AccountCodeEntity ac WHERE ac.user.userID =:id AND ac.account_segment =:sg")
    Optional<AccountCodeEntity> findAccountCodeEntityByUserIdAndAcctSegment(@Param("id") int userId, @Param("sg") int acctID);
}
