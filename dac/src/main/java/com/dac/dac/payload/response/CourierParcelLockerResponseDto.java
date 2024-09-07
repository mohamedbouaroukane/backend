package com.dac.dac.payload.response;

import com.dac.dac.entity.Address;
import com.dac.dac.utils.CustomDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierParcelLockerResponseDto {
    private int id;
    private String courierName;
    private String parcelLockerName;
    private int parcelLockerId;
    private int courierId;
    private Address parcelLockerAddress;
    private Date accessDate;
    private boolean isUsed;
    private String time;

}
