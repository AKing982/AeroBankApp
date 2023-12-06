package com.example.aerobankapp.scheduler.jobdetail;

import com.example.aerobankapp.configuration.QuartzConfig;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Getter
@Setter
public abstract class JobDetailBase<T extends TransactionBase>
{
    protected String jobName;
    protected static int jobCounter = 1;
    protected static int groupCounter = 1;
    private JobDetail jobDetail;
    private AnnotationConfigApplicationContext applicationContext;

    public JobDetailBase(String description)
    {
        this.jobName = description;
        initializeContext();
    }

    private void initializeContext()
    {
        applicationContext = new AnnotationConfigApplicationContext(QuartzConfig.class);
    }

    private JobDetailFactoryBean getJobDetailFactoryBeanTransaction(String name, T transaction, String data)
    {
        return (JobDetailFactoryBean) getApplicationContext().getBean(name, transaction, data);
    }

    private JobDetailFactoryBean getJobDetailFactoryBean(String name)
    {
        return (JobDetailFactoryBean) getApplicationContext().getBean(name);
    }

    public JobDetail getJobDetailFactoryBean(Class<? extends org.quartz.Job> job, JobDataMap dataMap, String description)
    {
       return (JobDetail) getApplicationContext().getBean("jobDetailFactoryBean", job, dataMap, description);
    }

    public JobDetail getJobDetailTransactionBean(T transaction, String data)
    {
        JobDetailFactoryBean jobDetailFactoryBean = null;
        if(transaction instanceof Deposit)
        {
            return getJobDetailFactoryBeanTransaction("depositJobDetailFactoryBean", transaction, data).getObject();
        }
        else if(transaction instanceof Withdraw)
        {
            return getJobDetailFactoryBeanTransaction("withdrawJobDetailFactoryBean", transaction, data).getObject();
        }
        else if(transaction instanceof Purchase)
        {
            return getJobDetailFactoryBeanTransaction("purchaseJobDetailFactoryBean", transaction, data).getObject();
        }
        return null;
    }

    public abstract JobDetail getJobDetail();

    public abstract JobDataMap getJobDataMap();

    public abstract void nullCheck(T transaction);

    public abstract void initialize(T transaction);

}
