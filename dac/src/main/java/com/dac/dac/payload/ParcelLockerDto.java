package com.dac.dac.payload;

import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.entity.Address;
import com.dac.dac.entity.Locker;
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
    private ParcelLockerStatus status;
    private Address address;

}
