package com.dac.dac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Address {
    @Id
    private int zipCode;
    private String street;
    private String city;
    private Double latitude;
    private Double longitude;

}
