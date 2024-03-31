package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransferType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Transfer extends TransactionBase implements Serializable
{
    private Long transferID;
    private int fromAccountID;
    private int toAccountID;
    private String toAccountNumber;
    private String toAccountCode;
    private int originUserID;
    private int targetUserID;
    private TransferType transferType;
    private boolean isPending;

    public Transfer(Long transferID, int fromAccountID, int toAccountID, int originUID, int targetUserID, TransferType transferType) {
        this.transferID = transferID;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.originUserID = originUID;
        this.targetUserID = targetUserID;
        this.transferType = transferType;
    }

    public Transfer(int userID,
                    String description,
                    BigDecimal amount,
                    LocalTime timeScheduled,
                    ScheduleType scheduleInterval,
                    LocalDate date_posted,
                    LocalDate dateScheduled,
                    Currency currency,
                    Long transferID,
                    int fromAccountID,
                    int toAccountID,
                    int originUserID,
                    int targetUserID,
                    TransferType transferType) {

        super(userID, description, amount, timeScheduled, scheduleInterval, date_posted, dateScheduled, currency);
        this.transferID = transferID;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.originUserID = originUserID;
        this.targetUserID = targetUserID;
        this.transferType = transferType;
    }

    /**
     * To be used with Same User Transfers
     */
    public Transfer(int userID, String description, BigDecimal amount, LocalTime timeScheduled, LocalDate dateScheduled, Long transferID, int fromAccountID, int toAccountID, TransferType transferType){
        super(userID, description, amount, timeScheduled, dateScheduled);
        this.transferID = transferID;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.transferType = transferType;
    }


    /**
     * To be used with UserToUser Transfers
     */
    public Transfer(String description, BigDecimal amount, LocalTime timeScheduled, LocalDate dateScheduled, Long transferID, int fromAccountID, int toAccountID, int originUserID, int targetUserID, String accountCode, String accountNumber, TransferType type){
        super(description, amount, timeScheduled, dateScheduled);
        this.transferID = transferID;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.originUserID = originUserID;
        this.targetUserID = targetUserID;
        this.toAccountCode = accountCode;
        this.toAccountNumber = accountNumber;
        this.transferType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;
        return fromAccountID == transfer.fromAccountID && toAccountID == transfer.toAccountID && originUserID == transfer.originUserID && targetUserID == transfer.targetUserID && transferType == transfer.transferType && Objects.equals(transferID, transfer.transferID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transferID, fromAccountID, toAccountID, originUserID, targetUserID, transferType);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferID=" + transferID +
                ", fromAccountID=" + fromAccountID +
                ", toAccountID=" + toAccountID +
                ", originUserID=" + originUserID +
                ", targetUserID=" + targetUserID +
                ", transferType=" + transferType +
                '}';
    }
}
