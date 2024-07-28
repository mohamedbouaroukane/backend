package com.dac.dac.payload.response;


import com.dac.dac.constants.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParcelLockerStaticsCourierResponseDto {

    private int id;
    private long sendingParcels;
    private long receivingParcel;
    private long parcelPrintedNumber;
    private String parcelLockerName;
    private String parcelLockerAddress;
    private Double parcelLockerLatitude;
    private Double parcelLockerLongitude;
    private double parcelLockerUsagePercentage;
    private double parcelLockerNotPrintedPercentage;
    private Priority parcelLockerPriority;

}
