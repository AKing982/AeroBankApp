package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.DepositRepository;
import com.example.aerobankapp.scheduler.*;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleParserImpl;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleValidator;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleValidatorImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.aerobankapp.services.utilities.DepositServiceUtil.*;

@Service
@Getter
public class DepositServiceImpl implements DepositService
{
    private final DepositRepository depositRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private AsyncDepositService asyncDepositService;

    private Logger LOGGER = LoggerFactory.getLogger(DepositServiceImpl.class);

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository,
                              EntityManager entityManager,
                              AsyncDepositService asyncDepositService)
    {
        this.depositRepository = depositRepository;
        this.entityManager = entityManager;
        this.asyncDepositService = asyncDepositService;
    }

    private void nullCheck(DepositsEntity deposits)
    {
        if(deposits == null)
        {
            throw new IllegalArgumentException("Invalid Deposit Entered...");
        }
    }

    @Override
    public List<DepositsEntity> findAll() {
        return getDepositRepository().findAll();
    }

    @Override
    public void save(DepositsEntity obj) {
        nullCheck(obj);
        getDepositRepository().save(obj);
    }

    @Override
    public void delete(DepositsEntity obj) {
        nullCheck(obj);
        getDepositRepository().delete(obj);
    }

    @Override
    public Optional<DepositsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DepositsEntity> findByUserName(String user) {
        if(user == null || user.isEmpty())
        {
            throw new IllegalArgumentException("Invalid user entered...");
        }
        return depositRepository.findDepositsByUserName(user);
    }

    @Override
    public List<DepositsEntity> findByUserID(int userID) {
        return depositRepository.findDepositsByUserID(userID);
    }

    @Override
    public List<DepositsEntity> getDepositsByUserNameDesc(String user)
    {
       return depositRepository.getDepositsByUserNameDesc(user);
    }

    @Override
    public List<DepositsEntity> getDepositsByAcctID(int acctID) {
        return depositRepository.getDepositsByAcctID(acctID);
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserIDASC(int id) {
       return depositRepository.getListOfDepositsByUserIDASC(id);
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserID_DESC(int id) {
        return depositRepository.getListOfDepositsByUserID_DESC(id);
    }

    @Override
    public void submit(DepositDTO request)
    {
        // Build the DepositsEntity
        buildAndSaveDepositEntity(request);
        SchedulerCriteria schedulerCriteria = buildAndProcessSchedulerCriteria(request);
        logSchedulerCriteria(schedulerCriteria);
        System.out.println("Deposit has been received");
    }

    private void buildAndSaveDepositEntity(DepositDTO request) {
        DepositsEntity depositsEntity = buildDepositEntity(request);
        getDepositRepository().save(depositsEntity);
    }

    private SchedulerCriteria buildAndProcessSchedulerCriteria(DepositDTO request) {
        SchedulerCriteria schedulerCriteria = buildCriteria(request);
        processSchedulerCriteriaAsync(schedulerCriteria, request);
        return schedulerCriteria;
    }

    private void processSchedulerCriteriaAsync(SchedulerCriteria schedulerCriteria, DepositDTO request) {
        asyncDepositService.validateAndParse(schedulerCriteria, triggerCriteria -> {
            Deposit depositDTO = buildDeposit(request);
            asyncDepositService.sendToRabbitMQ(depositDTO);
            try {
                asyncDepositService.startScheduler(triggerCriteria);

            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void logSchedulerCriteria(SchedulerCriteria schedulerCriteria) {
        LOGGER.info("ScheduleCriteria: " + schedulerCriteria);
        LOGGER.info("Scheduled Time: " + schedulerCriteria.getScheduledTime());
        LOGGER.info("scheduled Date: " + schedulerCriteria.getScheduledDate());
    }

}
