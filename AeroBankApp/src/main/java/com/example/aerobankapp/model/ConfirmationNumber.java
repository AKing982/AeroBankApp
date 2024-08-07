package com.example.aerobankapp.model;

import lombok.Data;

import java.util.Objects;

@Data
public class ConfirmationNumber
{
    private Integer confirmationValue;

    public ConfirmationNumber(Integer value){
        this.confirmationValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationNumber that = (ConfirmationNumber) o;
        return Objects.equals(confirmationValue, that.confirmationValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationValue);
    }
}
