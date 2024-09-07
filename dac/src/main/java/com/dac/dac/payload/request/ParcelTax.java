package com.dac.dac.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelTax {
    String parcelTrackingNumber;
    double weight;
}
