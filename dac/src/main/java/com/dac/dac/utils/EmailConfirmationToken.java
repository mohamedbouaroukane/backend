package com.dac.dac.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailConfirmationToken {

    private int userId;
    private String token;
    private LocalDateTime expiryTime;
    private LocalDateTime creationTime;

}
