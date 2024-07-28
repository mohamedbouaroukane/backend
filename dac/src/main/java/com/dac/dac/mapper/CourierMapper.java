package com.dac.dac.mapper;

import com.dac.dac.entity.Courier;
import com.dac.dac.payload.CourierDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public interface CourierMapper {

    public Courier mapToEntity(CourierDto courierDto);

    @Mapping(target = "password", ignore = true)
    public CourierDto mapToDto(Courier courier);

    public void updateEntity(@MappingTarget Courier courier, CourierDto courierDto);

}
