package com.dac.dac.mapper;

import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Parcel;
import com.dac.dac.payload.ParcelStatusDto;
import com.dac.dac.payload.request.ParcelRequestDto;
import com.dac.dac.payload.response.LocalParcelResponseDto;
import com.dac.dac.utils.ManifestModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LocalParcelMapper {


    ParcelStatusMapper parcelStatusMapper = Mappers.getMapper(ParcelStatusMapper.class);;


    @Mapping(source = "receiverParcelLockerId",target = "receiverParcelLocker.id")
    @Mapping(source = "senderId",target = "sender.id")
    // @Mapping(source = "parcelTypeId",target = "parcelDetail.id")
    public LocalParcel mapToEntity (ParcelRequestDto parcelRequestDto);

    @Mapping(source = "parcel",target = "statusList",qualifiedByName ="status")
    //@Mapping(source = "parcel",target = "statusList",qualifiedByName ="status")
    @Mapping(source = "parcelDetail.dimensions",target = "parcelType")
    @Mapping(source = "sender.fullName",target = "senderName")
    @Mapping(source = "sender.email",target = "senderEmail")
    @Mapping(source = "sender.phone",target = "senderPhone")
    public LocalParcelResponseDto mapToDto (LocalParcel parcel);


    public List<LocalParcelResponseDto> mapToDto(List<LocalParcel> parcels);

    @Mapping(source = "parcelDetail.dimensions.weight",target = "weight")
    public ManifestModel mapToManifestModel(Parcel parcel);

    public List<ManifestModel> mapToManifestModel(List<Parcel> parcels);
    @Named("status")
    default List<ParcelStatusDto> mapToDtoListStatus (Parcel parcel) {
        return parcelStatusMapper.mapToDto(parcel.getStatusList());
    }

}
