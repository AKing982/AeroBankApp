package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.exceptions.NullTriggerCriteriaException;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import lombok.Getter;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.util.UUID;

@Getter
public abstract class AbstractTriggerBase
{
    protected static final String triggerID = UUID.randomUUID().toString();
    protected static final String groupID = UUID.randomUUID().toString();
    protected TriggerCriteria triggerCriteria;

    public AbstractTriggerBase(TriggerCriteria triggerCriteria)
    {
        triggerCriteriaNullCheck(triggerCriteria);
    }

    protected void triggerCriteriaNullCheck(TriggerCriteria triggerCriteria)
    {
        if(triggerCriteria == null)
        {
            throw new NullTriggerCriteriaException("Trigger Criteria is null: ");
        }
        else
        {
            this.triggerCriteria = triggerCriteria;
        }
    }

    protected final TriggerKey getTriggerIdentity(Object id)
    {
        return TriggerKey.triggerKey(String.valueOf(id), String.valueOf(id));
    }

    protected final TriggerKey getTriggerIdentity(String triggerID, String groupID)
    {
        return TriggerKey.triggerKey(triggerID, groupID);
    }

    protected final TriggerKey getTriggerKey(Trigger trigger)
    {
        return trigger.getKey();
    }
}
