package com.dac.dac.mapper;

import com.dac.dac.entity.CourierParcelLocker;
import com.dac.dac.payload.response.CourierParcelLockerResponseDto;
import com.dac.dac.utils.CustomDate;
import org.mapstruct.*;

import java.util.Date;
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
    @Mapping(target = "parcelLockerId",source = "parcelLocker.id")
    @Mapping(target = "courierId",source = "courier.id")
    @Mapping(target = "time",source = "accessDate",qualifiedByName = "relativeTime")
    public CourierParcelLockerResponseDto mapToResponseDto(CourierParcelLocker courierParcelLocker);

    @Named("relativeTime")
    default String relativeTime(Date accessDate){
        return accessDate!=null ? CustomDate.getRelativeTime(accessDate) : "-";
    }
    List<CourierParcelLockerResponseDto> mapToListResponseDto(List<CourierParcelLocker> courierParcelLockers);
}
