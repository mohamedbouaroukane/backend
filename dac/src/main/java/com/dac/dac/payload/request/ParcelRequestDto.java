package com.dac.dac.payload.request;

import com.dac.dac.constants.DimensionsKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelRequestDto {
    private int id;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private int receiverParcelLockerId;
    private int senderId;
    private boolean withLabel ;
    private DimensionsKey parcelTypeId;
    private boolean withReservation;
    private ReservationRequestDto reservationRequestDto;

    private String internationalTrackingCode;
    private String sendingCountry;
    private String sendingCompany;
    private int zipCode;

    private String parcelType;

}
