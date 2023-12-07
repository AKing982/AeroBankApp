package com.example.aerobankapp.scheduler.jobdetail;

import com.example.aerobankapp.workbench.transactions.Purchase;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PurchaseJobDetail extends JobDetailBase<Purchase>
{
    private Purchase purchase;
    private JobDataMap purchaseData;

    @Autowired
    public PurchaseJobDetail(Purchase purchase)
    {
        super("Purchase Job");
        this.purchase = purchase;
    }

    @Override
    public void initialize(final Purchase purchase)
    {
        nullCheck(purchase);
        this.purchase = purchase;
    }

    @Override
    public void nullCheck(Purchase purchase)
    {

    }

    @Override
    public JobDetail getJobDetail()
    {
        return null;
    }

    @Override
    public JobDataMap getJobDataMap()
    {
        return purchaseData;
    }

    private JobDataMap getJobDataMap(Purchase transaction) {
        return null;
    }
}
