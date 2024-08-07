package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.sql.QueryComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLOperand implements QueryComponent
{
    private List<SQLCondition> components;
    private String operator;

    public SQLOperand(String operator){
        this.components = new ArrayList<>();
        this.operator = operator;
    }

    public void addComponent(SQLCondition component) {
        components.add(component);
    }

    public void addOperatorComponent(String operator){
        components.add(new SQLCondition(operator));
    }


    @Override
    public String buildQueryComponent() {
        if(components.isEmpty()){
            return "";
        }
        return components.stream()
                .map(QueryComponent::buildQueryComponent)
                .collect(Collectors.joining(" " + operator + " "));
    }
}
