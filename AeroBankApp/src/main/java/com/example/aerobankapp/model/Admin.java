package com.example.aerobankapp.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Builder
public class Admin implements UserModel
{
    @Id
    private int id;
    private String username;

    private
}
