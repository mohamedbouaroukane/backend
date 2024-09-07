package com.dac.dac.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CourierAccountResponseDto {

    private String courierName;
    private String courierEmail;
    private String courierPhone;
    private String password;
}
