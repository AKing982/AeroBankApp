package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;

public final class BillPaymentQueryBuilderUtil
{

    public static SQLTable buildTableStatement(String name, String alias){
        return new SQLTable(name, alias);
    }


    public static SQLSelect buildSelectStatementWithParameters(final String selection){
        SQLSelect select = new SQLSelect();
        select.addSelection(selection);
        return select;
    }
}
