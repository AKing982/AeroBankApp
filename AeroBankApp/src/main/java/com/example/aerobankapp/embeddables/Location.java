package com.example.aerobankapp.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Location
{
    private Long transactionID;

    @Column(name="address")
    @NotNull
    @NotBlank
    private String address;

    @Column(name="city")
    @NotBlank
    @NotNull
    private String city;

    @Column(name="region")
    @NotBlank
    @NotNull
    private String region;

    @Column(name="postalCode")
    @NotBlank
    @NotNull
    private String postalCode;

    @Column(name="country")
    @NotBlank
    @NotNull
    private String country;

    public Location(Long transactionID, String address, String city, String region, String postalCode, String country) {
        this.transactionID = transactionID;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
    }
}
