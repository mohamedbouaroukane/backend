package com.dac.dac.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelLockerStaticsResponseDto {

    private int id;
    private String parcelLockerName;
    private String parcelLockerAddress;
    private String lastTimeAccess;
    private double parcelLockerUsagePercentage;
    private int courierLastTimeAccessId;
    private String courierLastTimeAccessName;
}
