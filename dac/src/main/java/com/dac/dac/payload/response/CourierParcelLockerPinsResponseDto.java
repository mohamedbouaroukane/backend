package com.dac.dac.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CourierParcelLockerPinsResponseDto {

    private List<ParcelLockerPinResponseDto> sending;
    public List<ParcelLockerPinResponseDto> expired;
}
