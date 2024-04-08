package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.repositories.DepositRepository;

public interface DepositSubmitter
{
    void submit(DepositDTO depositDTO);
}
