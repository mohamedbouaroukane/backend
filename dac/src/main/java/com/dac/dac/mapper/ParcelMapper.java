package com.dac.dac.mapper;

import com.dac.dac.entity.InternationalParcel;
import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Parcel;
import com.dac.dac.payload.ParcelStatusDto;
import com.dac.dac.payload.response.ParcelListResponseDto;
import com.dac.dac.utils.CustomDate;
import com.dac.dac.utils.ManifestModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParcelMapper {

    ParcelStatusMapper parcelStatusMapper = Mappers.getMapper(ParcelStatusMapper.class);;


    @Mapping(source = "parcelDetail.dimensions.weight",target = "weight")
    public ManifestModel mapToManifestModel(Parcel parcel);

    public List<ManifestModel> mapToManifestModel(List<Parcel> parcels);

    @Named("status")
    default List<ParcelStatusDto> mapToDtoListStatus (Parcel parcel) {
        return parcelStatusMapper.mapToDto(parcel.getStatusList());
    }

    @Mapping(source = "status.statusLabel",target = "currentStatus")
    @Mapping(source = "parcelDetail.dimensions.id",target = "size")
    @Mapping(source = "parcel",target = "type", qualifiedByName = "type")
    @Mapping(source = "parcel",target = "senderName", qualifiedByName = "sender")
    @Mapping(source = "parcel",target = "createdAt", qualifiedByName = "date")
    ParcelListResponseDto mapToParcelListResponseDto (Parcel parcel);


    List<ParcelListResponseDto> mapToParcelListResponseDto (List<Parcel> parcels);


    @Named("type")
    default String mapToDtoType (Parcel parcel) {
        if(parcel instanceof InternationalParcel internationalParcel) {
            return "INTERNATIONAL";
        }else if(parcel instanceof LocalParcel localParcel) {
            return "LOCAL";
        }
        return "";
    }
    @Named("sender")
    default String mapToDtoSender (Parcel parcel) {
        if(parcel instanceof InternationalParcel internationalParcel) {
            return internationalParcel.getSendingCompany();
        }else if(parcel instanceof LocalParcel localParcel) {
            return localParcel.getSender().getFullName();
        }
        return "";
    }

    @Named("date")
    default String mapToDtoDate (Parcel parcel) {
        return CustomDate.dateTostring(parcel.getCreatedAt());
    }


}