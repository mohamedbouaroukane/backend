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
    private String postName;
    private String postAddress;
    private String communeName;
    private String dairaName;
    private String stateName;
    private Double latitude;
    private Double longitude;

}
