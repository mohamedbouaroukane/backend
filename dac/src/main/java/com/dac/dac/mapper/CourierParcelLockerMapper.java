package com.dac.dac.mapper;

import com.dac.dac.entity.CourierParcelLocker;
import com.dac.dac.payload.response.CourierParcelLockerResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CourierParcelLockerMapper {

    @Mapping(target = "courierName",source = "courier.fullName")
    @Mapping(target = "parcelLockerName",source = "parcelLocker.name")
    @Mapping(target = "parcelLockerAddress",source = "parcelLocker.address")
    public CourierParcelLockerResponseDto mapToResponseDto(CourierParcelLocker courierParcelLocker);

    List<CourierParcelLockerResponseDto> mapToListResponseDto(List<CourierParcelLocker> courierParcelLockers);
}
