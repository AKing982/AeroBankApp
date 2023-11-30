package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.utilities.BankAuthorization;
import javafx.scene.image.ImageView;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User implements Serializable
{
    private static long serialVersionUID = 1L;

    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private String password;
    private int pinNumber;
    private UserAuthority userAuthority;
}
