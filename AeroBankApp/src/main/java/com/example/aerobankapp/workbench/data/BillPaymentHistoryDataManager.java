package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.model.BillPaymentHistory;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.builder.BillPaymentHistoryEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BillPaymentHistoryDataManager
{
    private final BillPaymentHistoryService billPaymentHistoryService;
    private final EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentHistoryDataManager.class);

    @Autowired
    public BillPaymentHistoryDataManager(BillPaymentHistoryService billPaymentHistoryService,
                                         EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder){
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.billPaymentHistoryEntityBuilder = new BillPaymentHistoryEntityBuilderImpl();
    }

    public void validatePaymentHistoryInDB(Long paymentID){
        LOGGER.info("Validating Payment with ID: {}", paymentID);
        Optional<BillPaymentHistoryEntity> billPaymentEntityOptional = fetchBillPaymentHistoryById(paymentID);
        LOGGER.info("Validating payment is Present in the database");
//        if(paymentIsPresent(billPaymentEntityOptional)){
//            // Is the isProcessed field set to true
//            LOGGER.info("Payment has been processed");
//        }
    }

    public BillPaymentHistory createBillPaymentHistoryModel(ProcessedBillPayment processedBillPayment){
        return new BillPaymentHistory(processedBillPayment.getNextPaymentDate(), processedBillPayment.getLastProcessedDate(), LocalDate.now(), true);
    }

    public void saveBillPaymentHistory(final BillPaymentHistoryEntity billPaymentHistory){
        billPaymentHistoryService.save(billPaymentHistory);
    }

    public void updateIsProcessed(BillPaymentHistory billPaymentHistory){
        BillPaymentHistoryEntity billPaymentHistoryEntity = billPaymentHistoryService.findById(billPaymentHistory.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Bill payment history not found"));

        billPaymentHistoryEntity.setProcessed(true);
    }

    public Optional<BillPaymentHistoryEntity> fetchBillPaymentHistoryById(Long id){
        Optional<BillPaymentHistoryEntity> optionalBillPaymentHistoryEntity = billPaymentHistoryService.findById(id);
        if(optionalBillPaymentHistoryEntity.isPresent()){
            LOGGER.info("Fetching BillPaymentHistory: {}", optionalBillPaymentHistoryEntity.get());
        }else{
            LOGGER.warn("No BillPaymentHistory found for paymentID: {}",id);
        }
        return optionalBillPaymentHistoryEntity;
    }

    public boolean isPaymentProcessed(Long id){
        boolean isProcessed = billPaymentHistoryService.isPaymentProcessedById(id);
        LOGGER.info("Payment has been processed: {}", isProcessed);
        return isProcessed;
    }

    public void updateBillPaymentHistory(final BillPaymentHistory billPaymentHistory){
        BillPaymentHistoryEntity billPaymentHistoryEntity = billPaymentHistoryEntityBuilder.createEntity(billPaymentHistory);
        try{
            if(billPaymentHistoryEntity == null){
                throw new RuntimeException("Could not create bill payment history entity.");
            }

            // Save the bill payment history
            saveBillPaymentHistory(billPaymentHistoryEntity);

            // Retrieve the payment history id
            Long id = billPaymentHistoryEntity.getPaymentHistoryID();
            billPaymentHistory.setPaymentId(id);

        }catch(Exception e){
            LOGGER.error("There was an error updating the bill payment history", e);
        }
    }
}
