package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.sql.SQLStatement;

import java.util.ArrayList;
import java.util.List;

public class SQLSelect implements SQLStatement
{
    private List<String> selections;
    private final String SELECT = "SELECT";

    public SQLSelect(){
        this.selections = new ArrayList<>();
    }

    public void addSelection(String selection){
        selections.add(selection);
    }

    @Override
    public String buildSQL() {
       // return SELECT + String.join(", ", selections);
        return SELECT + " " + String.join(", ", selections);
    }
}
