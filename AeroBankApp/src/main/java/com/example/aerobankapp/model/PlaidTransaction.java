package com.example.aerobankapp.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor(access= AccessLevel.PUBLIC)
public class PlaidTransaction
{
    private Long id;
    private int userId;
    private String accountId;
    private String transactionId;
    private String transactionName;
    private String location;
    private List<String> categories;
    private LocalDate date;
    private boolean pending;
    private BigDecimal amount;

    public PlaidTransaction(Long id, int userId, String accountId, String transactionId, String transactionName, String location, List<String> categories, LocalDate date, boolean pending, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.transactionName = transactionName;
        this.location = location;
        this.categories = categories;
        this.date = date;
        this.pending = pending;
        this.amount = amount;
    }

    public PlaidTransaction(Long id, int userId, String accountId, String transactionName, String location, List<String> categories, LocalDate date, boolean pending, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.transactionName = transactionName;
        this.location = location;
        this.categories = categories;
        this.date = date;
        this.pending = pending;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaidTransaction that = (PlaidTransaction) o;
        return userId == that.userId && accountId == that.accountId && pending == that.pending && Objects.equals(id, that.id) && Objects.equals(transactionName, that.transactionName) && Objects.equals(location, that.location) && Objects.equals(categories, that.categories) && Objects.equals(date, that.date) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, accountId, transactionName, location, categories, date, pending, amount);
    }

    @Override
    public String toString() {
        return "PlaidTransaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", accountId=" + accountId +
                ", transactionName='" + transactionName + '\'' +
                ", location='" + location + '\'' +
                ", categories=" + categories +
                ", date=" + date +
                ", pending=" + pending +
                ", amount=" + amount +
                '}';
    }
}

