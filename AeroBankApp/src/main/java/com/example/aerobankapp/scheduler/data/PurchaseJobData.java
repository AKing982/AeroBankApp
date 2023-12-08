package com.example.aerobankapp.scheduler.data;

import com.example.aerobankapp.workbench.transactions.Purchase;
import lombok.Getter;
import lombok.Setter;
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
public class PurchaseJobData implements JobDataModel
{
    private JobDataMap jobDataMap;
    private Purchase purchase;
    private final String purchaseStr = "PurchaseData";

    @Autowired
    public PurchaseJobData(@Qualifier("purchase2") Purchase purchase)
    {
        initialize(purchase);
    }

    public void initialize(Purchase purchase)
    {
        if(purchase != null)
        {
            this.purchase = purchase;
            this.jobDataMap = getJobDataMap(purchase);
        }
    }

    private JobDataMap getJobDataMap(@Qualifier("purchase") Purchase purchase)
    {
        JobDataMap jobDataMap1 = new JobDataMap();
        jobDataMap1.put(purchaseStr, purchase);
        return jobDataMap1;
    }


    @Override
    public JobDataMap getJobDataMap()
    {
        return jobDataMap;
    }
}
