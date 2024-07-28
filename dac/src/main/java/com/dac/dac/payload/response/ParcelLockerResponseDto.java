package com.dac.dac.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class ParcelLockerResponseDto {
    private int pin;
}
