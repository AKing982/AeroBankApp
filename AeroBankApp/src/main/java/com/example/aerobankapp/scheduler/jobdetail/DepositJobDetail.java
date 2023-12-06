package com.example.aerobankapp.scheduler.jobdetail;

import com.example.aerobankapp.scheduler.data.DepositJobData;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.quartz.JobBuilder.newJob;


@Component
@Getter
@Setter
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepositJobDetail extends JobDetailBase<Deposit>
{
    private Deposit deposit;
    private JobDataMap depositJobDataMap;
    private DepositJobData depositJobData;
    private final String depositDataString = "DepositData";

    @Autowired
    public DepositJobDetail(@Qualifier("deposit") Deposit deposit)
    {
        super("DepositJob");
        initialize(deposit);
    }

    public void initializeJobData(Deposit deposit)
    {
        this.depositJobData = new DepositJobData(deposit);
    }

    @Override
    public void initialize(Deposit deposit)
    {
        if(deposit != null)
        {

        }
        nullCheck(deposit);
        initializeJobData(deposit);
        this.deposit = deposit;
        this.depositJobDataMap = depositJobData.getJobDataMap();
    }

    @Override
    public void nullCheck(Deposit deposit)
    {
        if(deposit == null)
        {
            throw new NullPointerException("Deposit is Null");
        }
    }


    @Override
    public JobDetail getJobDetail()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

        return newJob(DepositJob.class)
                .withIdentity("job" + randomNumber, "group" + randomNumber)
                .withDescription(getJobName())
                .usingJobData(getDepositJobDataMap())
                .build();
    }

    public JobDataMap getJobDataMap()
    {
        return getDepositJobDataMap();
    }

}
