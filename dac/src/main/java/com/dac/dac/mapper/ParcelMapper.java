package com.dac.dac.mapper;

import com.dac.dac.entity.Parcel;
import com.dac.dac.payload.ParcelStatusDto;
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
}