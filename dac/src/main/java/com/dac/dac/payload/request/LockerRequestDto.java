package com.dac.dac.payload.request;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.entity.Dimensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LockerRequestDto {
    private int id;
    private int pin;
    private int statusInt;
    private int parcelLockerId;
    private DimensionsKey lockerSizeID;

}
