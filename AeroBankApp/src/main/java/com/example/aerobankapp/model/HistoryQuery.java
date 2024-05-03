package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactionHistory.queries.TableConstants;
import com.example.aerobankapp.workbench.utilities.filters.TransactionHistoryFilterType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class HistoryQuery
{
    private TransactionHistoryFilterType filterType;
    private TableConstants tableConstants;
    private String query;

    public HistoryQuery(TransactionHistoryFilterType filterType, TableConstants tableConstants, String query){
        this.filterType = filterType;
        this.tableConstants = tableConstants;
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryQuery that = (HistoryQuery) o;
        return filterType == that.filterType && tableConstants == that.tableConstants && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterType, tableConstants, query);
    }

    @Override
    public String toString() {
        return "HistoryQuery{" +
                "filterType=" + filterType +
                ", tableConstants=" + tableConstants +
                ", query='" + query + '\'' +
                '}';
    }
}
