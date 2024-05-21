package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface BillPaymentService extends ServiceDAOModel<BillPaymentEntity>
{
    List<BillPayeeInfoDTO> findBillPaymentScheduleInfoByUserID(int userID);
}
