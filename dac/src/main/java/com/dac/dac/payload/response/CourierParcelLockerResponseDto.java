package com.dac.dac.payload.response;

import com.dac.dac.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierParcelLockerResponseDto {

    private String courierName;
    private String parcelLockerName;
    private Address parcelLockerAddress;
    private Date createDate;
}
