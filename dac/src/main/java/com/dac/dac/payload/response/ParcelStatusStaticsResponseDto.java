package com.dac.dac.payload.response;

import com.dac.dac.constants.ParcelStatusConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelStatusStaticsResponseDto {


    private ParcelStatusConst status;
    private Long numberOfParcels;
}
