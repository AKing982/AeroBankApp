package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.jobs.DepositJob;
import lombok.Getter;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.Random;

import static org.quartz.JobBuilder.newJob;

@Getter
public class DepositJobDetail extends AbstractJobDetail
{
    private DepositDTO depositDTO;

    public DepositJobDetail(DepositDTO depositDTO)
    {
        super("Deposit Job Data");
        this.depositDTO = depositDTO;
    }

    @Override
    public JobDetail getJobDetail() {
        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

        return newJob(DepositJob.class)
                .withIdentity("job" + randomNumber, "group" + randomNumber)
                .withDescription(description)
                .usingJobData(getDepositDataMap())
                .build();
    }

    public JobDataMap getDepositDataMap()
    {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("DepositData", getDepositDTO());
        return jobDataMap;
    }

}
