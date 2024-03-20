package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.repositories.WithdrawRepository;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.utilities.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.aerobankapp.services.utilities.WithdrawServiceUtil.*;

@Service
public class WithdrawServiceImpl implements WithdrawService
{
    private final WithdrawRepository withdrawRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Autowired
    public WithdrawServiceImpl(WithdrawRepository withdrawRepository){
        this.withdrawRepository = withdrawRepository;
    }

    @Override
    public List<WithdrawEntity> findAll() {
        List<WithdrawEntity> withdrawEntities = withdrawRepository.findAll();
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawRepository.findAll();
    }

    @Override
    public void save(WithdrawEntity obj) {
        if(obj != null){
            withdrawRepository.save(obj);
        }
    }

    @Override
    public void delete(WithdrawEntity obj) {
        if(obj != null){
            withdrawRepository.delete(obj);
        }
    }

    @Override
    public Optional<WithdrawEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<WithdrawEntity> findByUserName(String user)
    {
        assertEmptyUserString(LOGGER, user);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findWithdrawalsByUserName(user);

        // Assert the Withdraw Entity list is not empty
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);

        return withdrawRepository.findWithdrawalsByUserName(user);
    }

    @Override
    public List<WithdrawEntity> getListOfWithdrawalsByUserIDAsc(final int userID) {
        assertUserIDNotInvalid(userID);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByUserIDAscending(userID);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawEntities;
    }

    @Override
    public List<WithdrawEntity> getListOfWithdrawalsByUserIDDesc(final int userID) {
        assertUserIDNotInvalid(userID);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByUserIDDescending(userID);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawEntities;
    }

    @Override
    public Optional<WithdrawEntity> getWithdrawalByDescription(final String description) {
        assertDescriptionStringEmpty(description);

        Optional<WithdrawEntity> optionalWithdraw = withdrawRepository.findByDescription(description);

        assertOptionalWithdrawIsEmpty(optionalWithdraw);
        return optionalWithdraw;
    }

    @Override
    public BigDecimal getWithdrawalAmountById(Long id) {

        assertIDInvalid(id);
        BigDecimal withdrawalAmount = withdrawRepository.findAmountById(id);
        assertWithdrawAmountNull(id, withdrawalAmount);

        return withdrawalAmount;
    }

    @Override
    public List<WithdrawEntity> findByUserID(int userID) {
        assertUserIDNotInvalid(userID);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByUserId(userID);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawEntities;
    }

    @Override
    public List<WithdrawEntity> findByAccountID(final int acctID) {

        assertAccountIDInvalid(acctID);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByAccountId(acctID);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawEntities;
    }

    @Override
    public List<WithdrawEntity> findByStatus(final Status status) {
        assertStatusNotNull(status);
        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByStatus(status);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);

        return withdrawEntities;
    }

    @Override
    public List<WithdrawEntity> findWithdrawBetweenDates(LocalDate startDate, LocalDate endDate) {

        assertBeginAndEndDateNotNull(startDate, endDate);
        List<WithdrawEntity> withdrawEntities = withdrawRepository.findWithdrawsBetweenDates(startDate, endDate);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);
        return withdrawEntities;
    }


    @Override
    public List<WithdrawEntity> findWithdrawalsByAccountIDAndStatus(int acctID, Status status) {
        assertAccountIDInvalid(acctID);
        assertStatusNotNull(status);

        List<WithdrawEntity> withdrawEntities = withdrawRepository.findByAccountIdAndStatus(acctID, status);
        assertWithdrawEntityListNotEmpty(LOGGER, withdrawEntities);

        return withdrawEntities;
    }

}
