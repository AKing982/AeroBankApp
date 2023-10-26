package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.card.CardNumber;
import com.example.aerobankapp.model.User;;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class Card
{
    private int id;
    private User user;
    private String cardHolder;
    private CardNumber cardNo;
    private String expiration_date;
    private String issuedBank;
    private int cvv;
    private boolean cardStatus;

    public Card(String cardHolder, CardNumber cardNo, String expires, int cvv)
    {
        this.cardHolder = cardHolder;
        this.cardNo = cardNo;
        this.expiration_date = expires;
        this.cvv = cvv;
    }

    public boolean cardValidation(CardNumber cardNo, String exp, int cvv, String cardHolder)
    {
        if(cardNo == null && exp.equals(" ") && cvv == 0 && cardHolder.equals(" "))
        {
            return false;
        }
        return true;
    }
}
