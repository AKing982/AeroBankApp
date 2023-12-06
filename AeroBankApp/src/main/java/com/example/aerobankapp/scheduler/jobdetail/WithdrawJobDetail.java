package com.example.aerobankapp.scheduler.jobdetail;

import com.example.aerobankapp.workbench.transactions.Withdraw;
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
public class WithdrawJobDetail extends JobDetailBase<Withdraw>
{
    private Withdraw withdraw;
    private JobDataMap jobDataMap;
    private final String dataMapName = "WithdrawData";

    @Autowired
    public WithdrawJobDetail(Withdraw withdraw)
    {
        super("withdrawJob");
        initialize(withdraw);
    }

    @Override
    public void nullCheck(Withdraw withdraw)
    {
        if(withdraw == null)
        {
            throw new NullPointerException("Withdraw is null");
        }
    }

    @Override
    public void initialize(Withdraw withdraw)
    {
        nullCheck(withdraw);
        this.withdraw = withdraw;
        this.jobDataMap = getJobDataMap(withdraw);
    }

    @Override
    public JobDetail getJobDetail() {
        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

        return newJob(WithdrawJob.class)
                .withIdentity("job" + randomNumber, "group" + randomNumber)
                .withDescription(jobName)
                .usingJobData(getJobDataMap(withdraw))
                .build();
    }

    public JobDataMap getJobDataMap(@Qualifier("withdraw") Withdraw withdraw)
    {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(dataMapName, withdraw);
        return jobDataMap;
    }
}
