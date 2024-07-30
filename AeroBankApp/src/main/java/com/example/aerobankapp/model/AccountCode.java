package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AccountCode
{
    private String firstInitial;
    private String lastInitial;
    private int userID;
    private AccountType accountType;
    private int year;
    private int sequence;

    public AccountCode(){

    }

    public AccountCode(String firstInitial, String lastInitial, int userID, AccountType accountType, int year, int sequence) {
        this.firstInitial = firstInitial;
        this.lastInitial = lastInitial;
        this.userID = userID;
        this.accountType = accountType;
        this.year = year;
        this.sequence = sequence;
    }

    public String build(){
        StringBuilder code = new StringBuilder();
        code.append(firstInitial);
        code.append(lastInitial);
        code.append(accountType.name());
        code.append(" - ");
        code.append(year);
        code.append(sequence);
        return code.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCode that = (AccountCode) o;
        return userID == that.userID && year == that.year && sequence == that.sequence && Objects.equals(firstInitial, that.firstInitial) && Objects.equals(lastInitial, that.lastInitial) && accountType == that.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstInitial, lastInitial, userID, accountType, year, sequence);
    }

    @Override
    public String toString() {
        return "AccountCode{" +
                "firstInitial='" + firstInitial + '\'' +
                ", lastInitial='" + lastInitial + '\'' +
                ", userID=" + userID +
                ", accountType=" + accountType +
                ", year=" + year +
                ", sequence=" + sequence +
                '}';
    }

}
