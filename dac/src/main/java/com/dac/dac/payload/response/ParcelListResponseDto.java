package com.dac.dac.payload.response;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.constants.ParcelStatusConst;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor

public class ParcelListResponseDto {

    private int id;
    private String receiverName;
    private String senderName;
    private String type;
    private DimensionsKey size;
    private String trackingCode;
    private ParcelStatusConst currentStatus;
    private String createdAt;
}
