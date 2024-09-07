package com.dac.dac.payload.response;


import com.dac.dac.utils.CustomDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierAccessResponseDto {
    private String courierName;
    private String ParcelLockerName;
    private String parcelLockerStreet;

    @JsonIgnore
    private Date accessDate;
    private String time;

    public CourierAccessResponseDto(String courierName, String parcelLockerName, Date accessDate) {
        this.courierName = courierName;
        ParcelLockerName = parcelLockerName;
        this.accessDate = accessDate;
        time = accessDate!=null? CustomDate.getRelativeTime(accessDate):"-";
    }
}
