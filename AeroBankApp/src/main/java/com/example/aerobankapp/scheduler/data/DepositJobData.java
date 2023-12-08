package com.example.aerobankapp.scheduler.data;

import com.example.aerobankapp.workbench.transactions.Deposit;
import lombok.AllArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
@AllArgsConstructor
public class DepositJobData implements JobDataModel
{
    private JobDataMap jobDataMap;

    private Deposit deposit;
    private final String dataMapStr = "DepositData";

    @Autowired
    public DepositJobData(@Qualifier("deposit2") Deposit deposit)
    {
        initialize(deposit);
    }

    public void initialize(Deposit deposit)
    {
        this.jobDataMap = getJobDataMap(deposit);
    }

    private JobDataMap getJobDataMap(@Qualifier("deposit") Deposit deposit)
    {
        JobDataMap jobDataMap1 = new JobDataMap();
        jobDataMap1.put(dataMapStr, deposit);
        return jobDataMap1;
    }

    @Override
    public JobDataMap getJobDataMap()
    {
        return jobDataMap;
    }


}
