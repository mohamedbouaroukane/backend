package com.dac.dac.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParcelStaticsResponseDto {
    private Date date;
    private Long international;
    private Long local;


}
