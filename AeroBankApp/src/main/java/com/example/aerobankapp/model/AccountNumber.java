package com.example.aerobankapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountNumber
{
    private int first_segment;
    private int second_segment;
    private int third_segment;

    public AccountNumber(int branch, int iid, int code){
        this.first_segment = branch;
        this.second_segment = iid;
        this.third_segment = code;
    }

    public boolean isValid(){
        String accountNumber = getAccountNumberToString();
        String[] segments = accountNumber.split("-");

        if(segments.length == 3){
            if(first_segment > 0 && second_segment > 0 && third_segment > 0){
                if(segments[0].length() == 2 && segments[1].length() == 2 && segments[2].length() == 2){
                    return true;
                }
            }
        }
        return false;
    }

    public String getAccountNumberToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(first_segment);
        stringBuilder.append("-");
        stringBuilder.append(second_segment);
        stringBuilder.append("-");
        stringBuilder.append(third_segment);
        return stringBuilder.toString();
    }

}
