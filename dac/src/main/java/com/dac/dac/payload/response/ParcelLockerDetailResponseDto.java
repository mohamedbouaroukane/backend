package com.dac.dac.payload.response;

import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.constants.Priority;
import com.dac.dac.entity.Address;
import com.dac.dac.entity.Courier;
import lombok.*;
import lombok.experimental.SuperBuilder;



@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class ParcelLockerDetailResponseDto extends ParcelLockerResponseDto{

    private Courier accessCourier;
    private String lastTime;


    public ParcelLockerDetailResponseDto(int id, String name, int statusInt, ParcelLockerStatus status, Address address, Double usagePercentage, Priority priority) {
        super(id, name, statusInt, status, address, usagePercentage, priority);
    }
}
