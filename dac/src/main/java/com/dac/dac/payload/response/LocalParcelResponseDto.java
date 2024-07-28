package com.dac.dac.payload.response;


import com.dac.dac.entity.Dimensions;
import com.dac.dac.entity.ParcelLocker;
import com.dac.dac.payload.ParcelStatusDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalParcelResponseDto {

    private int id;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;

    private String senderName;
    private String senderEmail;
    private String senderPhone;

    @JsonIgnore
    private ParcelLocker receiverParcelLocker;
    private Dimensions parcelType;
    private byte[] lockerQRCode;
    private List<ParcelStatusDto> statusList;
}
