package com.dac.dac.payload;

import com.dac.dac.constants.DimensionsKey;
import com.dac.dac.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ParcelDto {

    private int id;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private String traceCode;
    private String lockerCode;

    @JsonIgnore
    private ParcelLocker reciverParcelLocker;
    private int receiverParcelLockerId;
    private int senderId;
    private DimensionsKey parcelTypeId;
    private Dimensions parcelType;
    private Client sender;

    private List<ParcelStatusDto> parcelStatuses;


}
