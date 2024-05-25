package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.exceptions.DuplicateBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.BillPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BillPaymentServiceImpl implements BillPaymentService
{
    private BillPaymentRepository billPaymentRepository;

    public BillPaymentServiceImpl(BillPaymentRepository billPaymentRepository){
        this.billPaymentRepository = billPaymentRepository;
    }

    @Override
    public List<BillPaymentEntity> findAll() {
        return null;
    }

    @Override
    public void save(BillPaymentEntity obj) {
        if(obj == null){
            throw new InvalidBillPaymentException("Bill Payment cannot be null");
        }

        if(obj.getAccount() == null ||
                obj.getPaymentType() == null ||
                obj.getPaymentAmount() == null ||
                obj.getPaymentSchedule() == null ||
                obj.getUser() == null ||
                obj.getPayeeName() == null){
            throw new InvalidBillPaymentParametersException("Bill Payment has null parameters.");
        }

        // Check for duplicates
        Optional<BillPaymentEntity> optionalBillPaymentEntity = billPaymentRepository.findById(obj.getPaymentID());
        if(optionalBillPaymentEntity.isPresent()){
            throw new DuplicateBillPaymentException("Found existing Bill Payment: " + optionalBillPaymentEntity.get());
        }else{
            billPaymentRepository.save(obj);
        }
    }



    @Override
    public void delete(BillPaymentEntity obj) {

    }

    @Override
    public Optional<BillPaymentEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPaymentEntity> findByUserName(String user) {
        // NOT IMPLEMENTED
        return null;
    }

    @Override
    @Transactional
    public List<BillPaymentEntity> findBillPaymentsByUserID(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Caught Invalid UserID: " + userID);
        }
        return billPaymentRepository.findBillPaymentsByUserID(userID);
    }
}
