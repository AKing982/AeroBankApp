package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.PendingTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingTransactionRepository extends JpaRepository<PendingTransactions, Long>
{

}
