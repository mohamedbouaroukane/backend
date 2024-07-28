package com.dac.dac.mapper;

import com.dac.dac.entity.InternationalParcel;
import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Parcel;
import com.dac.dac.payload.ParcelStatusDto;
import com.dac.dac.payload.request.ParcelRequestDto;
import com.dac.dac.payload.response.InternationalParcelResponseDto;
import com.dac.dac.payload.response.LocalParcelResponseDto;
import com.dac.dac.utils.ManifestModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InternationalParcelMapper {


    ParcelStatusMapper parcelStatusMapper = Mappers.getMapper(ParcelStatusMapper.class);;


    @Mapping(source = "receiverParcelLockerId",target = "receiverParcelLocker.id")
    public InternationalParcel mapToEntity (ParcelRequestDto parcelRequestDto);

    @Mapping(source = "parcel",target = "statusList",qualifiedByName ="status")
    @Mapping(source = "parcelDetail.dimensions",target = "parcelType")
    public InternationalParcelResponseDto mapToDto (InternationalParcel parcel);


    public List<InternationalParcelResponseDto> mapToDto(List<InternationalParcel> parcels);

    @Mapping(source = "parcelDetail.dimensions.weight",target = "weight")
    public ManifestModel mapToManifestModel(Parcel parcel);

    public List<ManifestModel> mapToManifestModel(List<Parcel> parcels);
    @Named("status")
    default List<ParcelStatusDto> mapToDtoListStatus (Parcel parcel) {
        return parcelStatusMapper.mapToDto(parcel.getStatusList());
    }

}
