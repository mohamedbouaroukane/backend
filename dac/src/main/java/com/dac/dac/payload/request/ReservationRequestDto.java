package com.dac.dac.payload.request;

import com.dac.dac.constants.DimensionsKey;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    private DimensionsKey parcelSize;
    private int senderParcelLockerId;
    private int parcelId;


}
