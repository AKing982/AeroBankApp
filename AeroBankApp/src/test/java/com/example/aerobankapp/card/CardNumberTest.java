package com.example.aerobankapp.card;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class CardNumberTest {

    @InjectMocks
    private CardNumber cardNumber;

    @BeforeEach
    void setUp()
    {
        CardType visa = CardType.VISA;
        String type = "758";
        String iin = "4758";
        String accountNumber = "0209 7760";
        cardNumber = new CardNumber(visa, type, iin, accountNumber);
    }

    @Test
    public void testNullCardNumber()
    {

        assertThrows(NullPointerException.class, () -> {
            cardNumber = new CardNumber(null, "2", "2", "2");
        });
    }

    @Test
    public void testVISACardSuccess()
    {
        CardType visa = CardType.VISA;
        String type = "";
        String iin = "758470";
        String accountNumber = "502097760";
        CardNumber cardNumber = new CardNumber(visa, type, iin, accountNumber);
        String expectedCardNumber = "4758-4705-0209-7760";

        assertEquals(expectedCardNumber, cardNumber.getCardNumber(4, type, iin, accountNumber));
    }

    @Test
    public void testGetTypeToDigits()
    {
        String type = "758";
        char[] typeDigits = cardNumber.getConversionToDigits(type);
        char[] expectedDigits = new char[]{'7', '5', '8'};

        assertArrayEquals(expectedDigits, typeDigits);
    }

    @Test
    public void testGetIdentifierToDigits()
    {
        String identifier = "758 470";
        char[] identifierDigits = cardNumber.getConversionToDigits(identifier);
        char[] expectedIdentDigits = new char[]{'7', '5', '8', ' ', '4', '7', '0'};

        assertArrayEquals(identifierDigits, expectedIdentDigits);
    }


    @AfterEach
    void tearDown() {
    }
}