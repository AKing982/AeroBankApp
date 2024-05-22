package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;

public interface BillPaymentQueryBuilder
{
    String buildQuery(SQLSelect select, SQLTable table, SQLOperand where);
}
