package com.dac.dac.payload.response;

import com.dac.dac.constants.LockerStatus;
import com.dac.dac.entity.Dimensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LockerResponseDto {

    private int id;
    private int pin;
    private LockerStatus status;
    private int parcelLockerId;
    private Dimensions lockerSize;

}
