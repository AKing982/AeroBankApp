package com.example.aerobankapp.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardNumber
{
    private CardType cardType;
    private String accountNumber;
    private String type;
    private String iin_number;
    private StringBuilder cardNumber;
    private String cardNo;
    private boolean isValid;
    private final int VISA_DESIGNATOR = 4;
    private final int AMEX_DESIGNATOR = 3;
    private final int MASTERCARD_DESIGNATOR = 5;
    private final int DISCOVER_DESIGNATOR = 6;

    public CardNumber(CardType ctype, String type, String iin_no, String accountNo) {
        initialize(ctype, type, iin_no, accountNo);
    }

    public void initialize(CardType card, String type, String iin, String acctNo) {
        boolean isValidCard = validInput(card, type, iin, acctNo);
        if (isValid) {
            this.type = type;
            this.iin_number = iin;
            this.accountNumber = acctNo;
            this.cardType = card;
            this.isValid = isValidCard;
            cardNo = initializeCard();
        }
        else
        {

        }
    }

    public boolean validInput(CardType ctype, String type, String iin, String accountNo) {
        if (ctype == null) {
            return false;
        } else if (ctype == CardType.VISA && type.length() == 3 && iin.length() == 3 && accountNo.length() == 10) {
            return true;
        } else if (ctype == CardType.AMEX && type.length() == 3 && iin.length() == 6 && accountNo.length() == 5) {
            return true;
        } else if (ctype == CardType.DISCOVER && type.length() == 3 && iin.length() == 4 && accountNo.length() == 4) {
            return true;
        }
        return false;
    }

    public String initializeCard() {
        String cardNumber = "";
        if (cardType == CardType.VISA) {
            cardNumber = getCardNumber(VISA_DESIGNATOR, type, iin_number, accountNumber);
        } else if (cardType == CardType.AMEX) {
            cardNumber = getCardNumber(AMEX_DESIGNATOR, type, iin_number, accountNumber);
        } else if (cardType == CardType.MASTERCARD) {
            cardNumber = getCardNumber(MASTERCARD_DESIGNATOR, type, iin_number, accountNumber);
        } else if (cardType == CardType.DISCOVER) {
            cardNumber = getCardNumber(DISCOVER_DESIGNATOR, type, iin_number, accountNumber);
        }
        return cardNumber;
    }

    public String getCardNumber()
    {
        return cardNumber.toString();
    }

    public char[] getTypeToDigits(final String type)
    {
        if(type.isEmpty() || type.equals(" "))
        {
            return null;
        }
        return type.toCharArray();
    }

    public char[] getIdentifierToDigits(final String identifier)
    {
        if(identifier.isEmpty() || identifier.equals(" "))
        {
            return null;
        }
        return identifier.toCharArray();
    }

    public char[] getAccountNumberToDigits(final String accountNumber)
    {
        if(accountNumber.isEmpty() || accountNumber.equals(" "))
        {
            return null;
        }
        return accountNumber.toCharArray();
    }


    public String getCardNumber(final int designator, final String type, final String iin, final String accountNo)
    {
        cardNumber = new StringBuilder();
        char[] typeDigits = getTypeToDigits(type);
        char[] iinDigits = getIdentifierToDigits(iin);
        char[] accountNumDigits = getAccountNumberToDigits(accountNo);

        switch(cardType)
        {
            case VISA:

                cardNumber.append(designator);
               // cardNumber.append()

        }
        cardNumber.append(designator);
        cardNumber.append(" ");
        cardNumber.append(type);
        cardNumber.append(" ");
        cardNumber.append(iin);
        cardNumber.append(" ");
        cardNumber.append(accountNo);
        return cardNumber.toString();
    }
}
