package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;

public class BillPaymentQueryBuilderImpl implements BillPaymentQueryBuilder
{

    public String getBillPaymentScheduleQuery(){
        return "";
    }

    public String getBillPaymentHistoryQuery(){
        return "";
    }

    public String getBillPaymentNotificationQuery(){
        return "";
    }

    @Override
    public String buildQuery(SQLSelect select, SQLTable table, SQLOperand where) {
        return null;
    }
}
