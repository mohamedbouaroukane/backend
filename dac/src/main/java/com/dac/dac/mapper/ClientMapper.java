package com.dac.dac.mapper;


import com.dac.dac.entity.Client;
import com.dac.dac.payload.ClientDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientMapper {

    public Client mapToEntity(ClientDto clientDto);

    @Mapping(target = "password", ignore = true)
    public ClientDto mapToDto(Client client);

    @Mapping(target = "password", ignore = true)
    public List<ClientDto> mapToDto(List<Client> clients);

    public void updateEntity(ClientDto clientDto, @MappingTarget Client client);



}