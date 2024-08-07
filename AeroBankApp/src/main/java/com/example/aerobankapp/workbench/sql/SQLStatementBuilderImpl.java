package com.example.aerobankapp.workbench.sql;

import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;

public class SQLStatementBuilderImpl implements SQLStatementBuilder
{
    private SQLSelect sqlSelect;
    private SQLTable sqlTable;
    private SQLOperand whereConditions;

    public SQLStatementBuilderImpl(SQLSelect sqlSelect, SQLTable sqlTable, SQLOperand where){
        this.sqlSelect = sqlSelect;
        this.sqlTable = sqlTable;
        this.whereConditions = where;
    }

    @Override
    public String buildQuery() {
        return sqlSelect.buildSQL() + " " + sqlTable.buildSQL() + " WHERE " + whereConditions.buildQueryComponent();
    }
}
