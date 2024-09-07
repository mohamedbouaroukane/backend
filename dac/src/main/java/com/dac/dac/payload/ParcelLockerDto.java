package com.dac.dac.payload;

import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.entity.Address;
import com.dac.dac.entity.Locker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class ParcelLockerDto {
    private int id;
    private String name;
    private int statusInt;
    private ParcelLockerStatus status;
    private List<Locker> lockers;
    private Address address;
    private int courier;

}
