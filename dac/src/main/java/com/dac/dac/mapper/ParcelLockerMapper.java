package com.dac.dac.mapper;

import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.entity.ParcelLocker;
import com.dac.dac.entity.ParcelStatus;
import com.dac.dac.payload.ParcelLockerDto;
import com.dac.dac.payload.response.ParcelLockerDetailResponseDto;
import com.dac.dac.payload.response.ParcelLockerResponseDto;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)public interface ParcelLockerMapper {

    @Mapping(source="manager.id",target = "managerId")
    ParcelLockerResponseDto mapToDto(ParcelLocker parcelLocker);

    ParcelLockerDetailResponseDto mapToDetailedDto(ParcelLocker parcelLocker);
    @Mapping(source = "parcelLockerDto", target = "status", qualifiedByName = "status")
    @Mapping(source = "courier", target = "manager.id")
    ParcelLocker mapToEntity(ParcelLockerDto parcelLockerDto);

    @Mapping(source = "parcelLockerDto", target = "status", qualifiedByName = "status")
    public void updateEntity(@MappingTarget ParcelLocker parcelLocker, ParcelLockerDto parcelLockerDto);

    @Named("status")
    default ParcelLockerStatus maoToDateDto(ParcelLockerDto parcelLockerDto){

        return switch (parcelLockerDto.getStatusInt()) {
            case 1 -> ParcelLockerStatus.AVAILABLE;
            default -> ParcelLockerStatus.NOT_AVAILABLE;
        };
    }

}
