package com.dac.dac.mapper;

import com.dac.dac.entity.ParcelLocker;
import com.dac.dac.payload.ParcelLockerDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)public interface ParcelLockerMapper {

    ParcelLockerDto mapToDto(ParcelLocker parcelLocker);

    ParcelLocker mapToEntity(ParcelLockerDto parcelLockerDto);

    public void updateEntity(@MappingTarget ParcelLocker parcelLocker, ParcelLockerDto parcelLockerDto);
}
