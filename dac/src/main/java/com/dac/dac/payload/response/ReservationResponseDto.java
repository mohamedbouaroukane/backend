package com.dac.dac.payload.response;

import com.dac.dac.payload.ParcelLockerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private LocalDateTime expiredIn;
    private Boolean isUsed;
    private Boolean isExpired;

    private String parcelLockerName;
    private Double parcelLockerLatitude;
    private Double parcelLockerLongitude;

}
