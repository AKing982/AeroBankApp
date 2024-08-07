package com.example.aerobankapp.dto;

import com.example.aerobankapp.card.CardNumber;
import lombok.Builder;

import javax.swing.text.html.ImageView;

@Builder
public record CardDesignatorDTO(int id,
                                UserDTO user,
                                String cardHolder,
                                CardNumber cardNumber,
                                String expirationDate,
                                String issuer,
                                int cvv,
                                boolean cardStatus,
                                ImageView cardImage)
{

}
