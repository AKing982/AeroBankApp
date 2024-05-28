package com.example.aerobankapp.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class BillPayment
{
    private String payeeName;
    private AccountCode accountCode;
    private BigDecimal paymentAmount;

    private String paymentType;

    private LocalDate posted;

    public BillPayment(String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate posted) {
        this.payeeName = payeeName;
        this.accountCode = accountCode;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.posted = posted;
    }

    public BillPayment(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPayment that = (BillPayment) o;
        return Objects.equals(payeeName, that.payeeName) && Objects.equals(accountCode, that.accountCode) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(paymentType, that.paymentType) && Objects.equals(posted, that.posted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payeeName, accountCode, paymentAmount, paymentType, posted);
    }
}
