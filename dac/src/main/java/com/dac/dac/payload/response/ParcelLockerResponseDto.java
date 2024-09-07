package com.dac.dac.payload.response;

import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.constants.Priority;
import com.dac.dac.entity.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelLockerResponseDto {
    private int id;
    private String name;
    @JsonIgnore
    private int statusInt;
    private ParcelLockerStatus status;
    private Address address;
    private Double usagePercentage;
    private Priority priority;
    private int managerId;

    public ParcelLockerResponseDto(int id, String name, int statusInt, ParcelLockerStatus status, Address address, Double usagePercentage, Priority priority) {
        this.id = id;
        this.name = name;
        this.statusInt = statusInt;
        this.status = status;
        this.address = address;
        this.usagePercentage = usagePercentage;
        this.priority = priority;
    }
}
