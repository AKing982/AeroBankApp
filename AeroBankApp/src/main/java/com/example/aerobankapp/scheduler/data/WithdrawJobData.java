package com.example.aerobankapp.scheduler.data;

import com.example.aerobankapp.workbench.transactions.Withdraw;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
@Setter
public class WithdrawJobData implements JobDataModel
{
    private JobDataMap jobDataMap;
    private Withdraw withdraw;
    private final String dataMapStr = "WithdrawData";

    @Autowired
    public WithdrawJobData(Withdraw withdraw)
    {
        initialize(withdraw);
    }

    public void initialize(final Withdraw withdraw)
    {
        if(withdraw != null)
        {
            this.withdraw = withdraw;
            this.jobDataMap = getJobDataMap(withdraw);
        }
    }

    private JobDataMap getJobDataMap(@Qualifier("withdraw") Withdraw withdraw)
    {
        JobDataMap jobDataMap1 = new JobDataMap();
        jobDataMap1.put(dataMapStr, withdraw);
        return jobDataMap1;
    }

    @Override
    public JobDataMap getJobDataMap()
    {
        return jobDataMap;
    }
}
