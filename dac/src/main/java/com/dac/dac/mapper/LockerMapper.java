package com.dac.dac.mapper;

import com.dac.dac.entity.Locker;
import com.dac.dac.payload.LockerDto;
import com.dac.dac.payload.response.LockerResponseDto;
import com.dac.dac.payload.response.ParcelLockerResponseDto;
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
    public Locker mapToEntity(LockerDto lockerDto);

    @Mapping(target = "parcelLockerId",source = "parcelLocker.id")
    public LockerResponseDto mapToDto(Locker locker);

    public List<LockerResponseDto> mapToDto(List<Locker> lockers);

    public void updateEntity(LockerDto lockerDto,@MappingTarget Locker locker);


    public List<ParcelLockerResponseDto> mapToParcelLockerResponseDto(List<Locker> lockers);

}
