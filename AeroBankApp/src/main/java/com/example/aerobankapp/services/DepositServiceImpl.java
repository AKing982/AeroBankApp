package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.DepositsEntity;
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

    @Override
    public List<DepositsEntity> findAll() {
        return getDepositRepository().findAll();
    }

    @Override
    public void save(DepositsEntity obj) {
        getDepositRepository().save(obj);
    }

    @Override
    public void delete(DepositsEntity obj) {
        getDepositRepository().delete(obj);
    }

    @Override
    public Optional<DepositsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DepositsEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByUserNameDesc(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByAcctID(int acctID) {
        TypedQuery<DepositsEntity> depositsEntityTypedQuery = getEntityManager()
                .createQuery("SELECT d FROM DepositsEntity d JOIN d.account a WHERE a.acctID = :acctID", DepositsEntity.class);
        depositsEntityTypedQuery.setParameter("acctID", acctID);
        depositsEntityTypedQuery.setMaxResults(30);
        return depositsEntityTypedQuery.getResultList();
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserIDASC(Long id) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserID_DESC(Long id) {
        return null;
    }

    @Override
    public void submit(DepositDTO request)
    {
        // Build the SchedulerCriteria
        SchedulerCriteria schedulerCriteria = buildCriteria(request);

        asyncDepositService.validateAndParse(schedulerCriteria, (triggerCriteria) -> {
            // Callback with the result of validation and parsing
            Deposit depositDTO = buildDeposit(request);

            // Send the DepositDTO to RabbitMQ asynchronously
            asyncDepositService.sendToRabbitMQ(depositDTO);

            // Start the Quartz Scheduler asynchronously
            asyncDepositService.startScheduler(triggerCriteria);
        });

        System.out.println(schedulerCriteria.toString());
        System.out.println(schedulerCriteria.getScheduledTime());
        System.out.println(schedulerCriteria.getScheduledDate());
        System.out.println("Deposit has been recieved");
    }

    private SchedulerCriteria buildCriteria(DepositDTO request)
    {
        LOGGER.debug("Request Date: " + request.getDate());
        LOGGER.debug("Request Time: " + request.getTimeScheduled());
        return SchedulerCriteria.builder()
                .scheduledDate(request.getDate())
                .scheduledTime(request.getTimeScheduled())
                .scheduleType(request.getScheduleInterval())
                .priority(1)
                .createdAt(LocalDate.now())
                .build();
    }

    private Deposit buildDeposit(DepositDTO request)
    {
        Deposit deposit = new Deposit();
        deposit.setDepositID(request.getDepositID());
        deposit.setAcctCode(request.getAccountCode());
        deposit.setAmount(request.getAmount());
        deposit.setDescription(request.getDescription());
        deposit.setAccountID(request.getAccountID());
        deposit.setDateScheduled(request.getDate());
        deposit.setScheduleInterval(request.getScheduleInterval());
        deposit.setTimeScheduled(request.getTimeScheduled());
        deposit.setDate_posted(LocalDate.now());
        deposit.setUserID(request.getUserID());
        return deposit;
    }
}
