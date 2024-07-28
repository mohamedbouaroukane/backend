package com.dac.dac.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierParcelLockerCheckerModel {

    private Date createDate;
    private Date expiryDate;
    private int parcelLockerId;
    private int courierId;

}
