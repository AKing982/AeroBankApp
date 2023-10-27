package com.example.aerobankapp.card;

import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import javafx.scene.image.ImageView;
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
    private static final int VISA_DESIGNATOR = 4;
    private static final int AMEX_DESIGNATOR = 3;
    private static final int MASTERCARD_DESIGNATOR = 5;
    private static final int DISCOVER_DESIGNATOR = 6;
    private AeroLogger aeroLogger = new AeroLogger(CardNumber.class);

    public CardNumber(CardType ctype, String type, String iin_no, String accountNo) {
        initialize(ctype, type, iin_no, accountNo);
    }

    public void initialize(CardType card, String type, String iin, String acctNo) {
        boolean isValidCard = validInput(card, type, iin, acctNo);
        this.cardType = card;
        if(isValidCard) {
            aeroLogger.info("Card Type: " + cardType);
            this.type = type;
            this.iin_number = iin;
            this.accountNumber = acctNo;
            this.isValid = isValidCard;
            cardNo = initializeCard();
        }

    }

    public boolean validInput(CardType ctype, String type, String iin, String accountNo) {
        if (ctype == null) {
            aeroLogger.info("CardType: " + ctype);
            return false;
        } else if (ctype == CardType.VISA && type.isEmpty() && iin.length() == 6 && accountNo.length() == 9) {
            return true;
        } else if (ctype == CardType.AMEX && type.length() == 4 && iin.length() == 6 && accountNo.length() == 4) {
            return true;
        } else if (ctype == CardType.DISCOVER && type.length() == 1 && iin.length() == 6 && accountNo.length() == 8) {
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

    public char[] getConversionToDigits(final String param)
    {
        if(param.isEmpty() || param.equals(" "))
        {
            return null;
        }
        return param.toCharArray();
    }

    private void appendDigits(StringBuilder builder, char[] digits, int start, int end)
    {
        for(int i = start; i < end; i++)
        {
            builder.append(digits[i]);
        }
    }

    private StringBuilder appendDigits(char[] digits, int start, int end)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < end; i++)
        {
            builder.append(digits[i]);
        }
        return builder;
    }

    private StringBuilder getSegment(char[] digits, int start, int end)
    {
        return appendDigits(digits, start, end);
    }

    private String[] convertCardNumberToSegments(StringBuilder builder)
    {
        String convertedString = builder.toString();
        return convertedString.split("-");
    }

    public boolean validateCardNumberFormat(StringBuilder card)
    {
        boolean result = false;
        String[] segments = convertCardNumberToSegments(card);
        switch(cardType)
        {
            case VISA:
                String iinSeg = segments[0];
                String accountNumSeg = segments[1];
                result = iinSeg.length() == 6 && accountNumSeg.length() == 8;
                break;
            case AMEX:

        }
    }

    public String getCardNumber(final int designator, final String type, final String iin, final String accountNo)
    {
        cardNumber = new StringBuilder();
        char[] typeDigits = getConversionToDigits(type);
        char[] iinDigits = getConversionToDigits(iin);
        char[] accountNumDigits = getConversionToDigits(accountNo);

        switch(cardType)
        {
            case AMEX:
                cardNumber.append(designator);
                appendDigits(cardNumber, typeDigits, 0, 3);
                cardNumber.append("-");
                appendDigits(cardNumber, typeDigits, 3, 4);
                appendDigits(cardNumber, iinDigits, 0, 5);
                cardNumber.append("-");
                appendDigits(cardNumber, iinDigits, 5, 6);
                appendDigits(cardNumber, accountNumDigits, 0, 4);
                aeroLogger.info("AMEX Card Number: " + cardNumber.toString());
                return cardNumber.toString();
            case VISA:
                cardNumber.append(designator);
                appendDigits(cardNumber, iinDigits, 0, 3);
                cardNumber.append("-");
                appendDigits(cardNumber, iinDigits, 3, 6);
                appendDigits(cardNumber, accountNumDigits, 0, 1);
                cardNumber.append("-");
                appendDigits(cardNumber, accountNumDigits, 1, 5);
                cardNumber.append("-");
                appendDigits(cardNumber, accountNumDigits, 5, 9);
                aeroLogger.info("VISA Card Number: " + cardNumber.toString());
                return cardNumber.toString();
            case MASTERCARD:
                cardNumber.append(designator);
                appendDigits(cardNumber, typeDigits, 0, 2);
                appendDigits(cardNumber, iinDigits, 1, 3);
                cardNumber.append("-");
                appendDigits(cardNumber, iinDigits, 2, 6);
                cardNumber.append("-");
                appendDigits(cardNumber, accountNumDigits, 0, 4);
                cardNumber.append("-");
                appendDigits(cardNumber, accountNumDigits, 5, 8);
                aeroLogger.info("MasterCard Number: " + cardNumber.toString());
                return cardNumber.toString();

            case DISCOVER:
                cardNumber.append(designator);

        }
        return null;
    }
}
