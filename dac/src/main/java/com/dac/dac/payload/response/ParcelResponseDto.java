package com.dac.dac.payload.response;

import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Parcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ParcelResponseDto {

    private List<LocalParcelResponseDto> localParcels;
    private List<InternationalParcelResponseDto> internationalParcels;

    public ParcelResponseDto() {
        this.localParcels = new ArrayList<>();
        this.internationalParcels = new ArrayList<>();
    }
}
