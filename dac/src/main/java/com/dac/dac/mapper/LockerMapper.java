package com.dac.dac.mapper;

import com.dac.dac.constants.LockerStatus;
import com.dac.dac.constants.ParcelLockerStatus;
import com.dac.dac.entity.Locker;
import com.dac.dac.payload.LockerDto;
import com.dac.dac.payload.ParcelLockerDto;
import com.dac.dac.payload.request.LockerRequestDto;
import com.dac.dac.payload.response.LockerResponseDto;
import com.dac.dac.payload.response.ParcelLockerPinResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LockerMapper {

    @Mapping(source = "parcelLockerId",target = "parcelLocker.id")
    @Mapping(source = "lockerSizeID",target = "lockerSize.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "lockerDto", target = "status", qualifiedByName = "status")
    public Locker mapToEntity(LockerRequestDto lockerDto);

    @Mapping(target = "parcelLockerId",source = "parcelLocker.id")
    @Mapping(target = "parcelLockerName",source = "parcelLocker.name")
    public LockerResponseDto mapToDto(Locker locker);

    public List<LockerResponseDto> mapToDto(List<Locker> lockers);

    @Mapping(source = "lockerDto", target = "status", qualifiedByName = "status")
    public void updateEntity(LockerRequestDto lockerDto,@MappingTarget Locker locker);


    public List<ParcelLockerPinResponseDto> mapToParcelLockerResponseDto(List<Locker> lockers);
    @Named("status")
    default LockerStatus maoToDateDto(LockerRequestDto lockerDto){

        return switch (lockerDto.getStatusInt()) {
            case 1 -> LockerStatus.AVAILABLE;
            default -> LockerStatus.NOT_AVAILABLE;
        };
    }
}
