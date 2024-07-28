package com.dac.dac.payload;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.entity.Dimensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class LockerDto {

    private int id;
    private int pin;
    private LockerStatus status;
    private int parcelLockerId;

    private DimensionsKey lockerSizeID;
    private Dimensions lockerSize;

}
