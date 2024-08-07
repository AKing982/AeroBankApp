package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class QueryStatement
{
    private String query;
    private List<Object> parameters;

    public QueryStatement(String sql)
    {
        this.query = sql;
    }

    public void addParameter(Object param)
    {
        this.parameters.add(param);
    }
}
