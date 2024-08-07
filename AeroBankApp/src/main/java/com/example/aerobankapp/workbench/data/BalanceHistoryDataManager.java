package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.services.BalanceHistoryService;
import com.example.aerobankapp.services.builder.BalanceHistoryEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceHistoryDataManager {

    private BalanceHistoryService balanceHistoryService;
    private EntityBuilder<BalanceHistoryEntity, BalanceHistory> entityBalanceHistoryEntityBuilder;
    private Logger LOGGER = LoggerFactory.getLogger(BalanceHistoryDataManager.class);

    @Autowired
    public BalanceHistoryDataManager(BalanceHistoryService balanceHistoryService,
                                     EntityBuilder<BalanceHistoryEntity, BalanceHistory> entityBalanceHistoryEntityBuilder){
        this.balanceHistoryService = balanceHistoryService;
        this.entityBalanceHistoryEntityBuilder = new BalanceHistoryEntityBuilderImpl();
    }


    public BalanceHistory createBalanceHistoryModel(BigDecimal newBalance, BigDecimal adjusted, BigDecimal prevBalance, int acctID){
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setNewBalance(newBalance);
        balanceHistory.setCurrentBalance(prevBalance);
        balanceHistory.setAdjustedAmount(adjusted);
        balanceHistory.setAccountID(acctID);
        return balanceHistory;
    }

    public void updateBalanceHistory(final BalanceHistory balanceHistory){
        BalanceHistoryEntity balanceHistoryEntity = buildBalanceHistoryEntity(balanceHistory);
        try{
            if(balanceHistoryEntity == null){
                throw new RuntimeException("Unable to create a new balance history entity.");
            }
            saveBalanceHistory(balanceHistoryEntity);

        }catch(Exception e){
            LOGGER.error("There was an error updating the balance history", e);
        }
    }

    private void saveBalanceHistory(BalanceHistoryEntity balanceHistoryEntity){
        balanceHistoryService.save(balanceHistoryEntity);
    }

    private BalanceHistoryEntity buildBalanceHistoryEntity(BalanceHistory balanceHistory){
        return entityBalanceHistoryEntityBuilder.createEntity(balanceHistory);
    }

}
