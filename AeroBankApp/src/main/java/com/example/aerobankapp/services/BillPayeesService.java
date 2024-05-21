package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;


public interface BillPayeesService extends ServiceDAOModel<BillPayeesEntity>
{
    List<BillPayeesEntity> findBillPayeesByUserID(int userID);

    List<String> findBillPayeeNamesByUserID(int userID);
}
