package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.sql.QueryComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SQLCondition implements QueryComponent
{
    private String field;
    private String operator;
    private String value;
    private String rangeCondition;

    public SQLCondition(String field, String operator, String value){
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * DO NOT ATTEMPT TO USE OR NULL POINTER WILL BE THROWN
     * @param range
     */
    public SQLCondition(String range){
        this.rangeCondition = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLCondition that = (SQLCondition) o;
        return Objects.equals(field, that.field) && Objects.equals(operator, that.operator) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operator, value);
    }

    @Override
    public String toString() {
        return "SQLCondition{" +
                "field='" + field + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public String buildQueryComponent() {
       // return field + " " + operator + " " + value;
        if(operator.equals("LIKE") || operator.equals("BETWEEN")){
            return field + " " + operator + " " + value;
        }
        else{
            return field + " " + operator + value;
        }
    }
}
