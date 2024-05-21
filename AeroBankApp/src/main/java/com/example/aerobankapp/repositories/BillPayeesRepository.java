package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPayeesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPayeesRepository extends JpaRepository<BillPayeesEntity, Long>
{
    @Query("SELECT e FROM BillPayeesEntity e WHERE e.user.userID=:userID")
    List<BillPayeesEntity> findBillPayeesEntitiesByUserID(@Param("userID") int userID);

    @Query("SELECT e.payeeName FROM BillPayeesEntity e WHERE e.user.userID=:userID")
    List<String> findBillPayeeNamesByUserID(@Param("userID") int userID);
}
