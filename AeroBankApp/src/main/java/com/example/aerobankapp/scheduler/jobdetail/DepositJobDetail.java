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
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.quartz.JobBuilder.newJob;


@Component
@Getter
@Setter
public class DepositJobDetail extends JobDetailBase<Deposit>
{
    private Deposit deposit;
    private JobDataMap depositJobDataMap;
    private JobDetail jobDetail;
    private final String depositDataString = "DepositData";

    @Autowired
    public DepositJobDetail(@Qualifier("deposit") Deposit deposit)
    {
        super("DepositJob");
        initialize(deposit);
    }

    @Override
    public void initialize(Deposit deposit)
    {
        nullCheck(deposit);
        this.deposit = deposit;

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

        JobDetail depositJobDetail = super.getJobDetailFactoryBean(DepositJob.class, depositJobDataMap, jobName);

        return newJob(DepositJob.class)
                .withIdentity("job" + randomNumber, "group" + randomNumber)
                .withDescription(jobName)
                .usingJobData(depositJobDataMap)
                .build();
    }

    public JobDataMap getJobDataMap()
    {
        return depositJobDataMap;
    }

}
