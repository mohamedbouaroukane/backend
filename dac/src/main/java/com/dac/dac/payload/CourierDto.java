package com.dac.dac.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierDto {
    int id;
    String fullName;
    String email;
    String phone;
    String password;
}
