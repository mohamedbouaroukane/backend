package com.dac.dac.mapper;

import com.dac.dac.entity.Reservation;
import com.dac.dac.payload.request.ReservationRequestDto;
import com.dac.dac.payload.response.ReservationResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationMapper {

    @Mapping(source = "parcelId",target = "parcel.id")
    public Reservation mapToEntity(ReservationRequestDto reservation);

    @Mapping(source = "locker.parcelLocker.name",target = "parcelLockerName")
    @Mapping(source = "locker.parcelLocker.latitude",target = "parcelLockerLatitude")
    @Mapping(source = "locker.parcelLocker.longitude",target = "parcelLockerLongitude")
    public ReservationResponseDto mapToDto(Reservation reservation);

    public List<ReservationResponseDto> mapToDto(List<Reservation> reservations);

}
