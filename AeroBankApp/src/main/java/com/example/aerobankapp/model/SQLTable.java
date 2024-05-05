package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.sql.SQLStatement;
import org.springframework.stereotype.Component;

public class SQLTable implements SQLStatement
{
    private String tableName;
    private String alias;

    public SQLTable(String tableName){
        this.tableName = tableName;
    }

    public SQLTable(String tableName, String alias){
        this.tableName = tableName;
        this.alias = alias;
    }

    @Override
    public String buildSQL() {
        if(alias.isEmpty()){
            return "FROM " + tableName;
        }else{
            return "FROM " + tableName + " " + alias;
        }
    }
}
