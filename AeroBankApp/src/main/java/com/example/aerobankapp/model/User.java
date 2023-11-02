package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import javafx.scene.image.ImageView;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@Component
@AllArgsConstructor
public class User implements Serializable, UserModel
{
    private static long serialVersionUID = 1L;

    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private char[] password;
    private int pinNumber;
    private UserAuthority userAuthority;
    private List<CardDesignator> cards = new ArrayList<>();
    private List<ImageView> cardImages = new ArrayList<>();

}
