package com.dac.dac.service;

import com.dac.dac.constants.*;
import com.dac.dac.entity.*;
import com.dac.dac.exption.EmptyListRecordException;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.ParcelLockerMapper;
import com.dac.dac.payload.ParcelLockerDto;
import com.dac.dac.payload.response.ParcelLockerStaticsResponseDto;
import com.dac.dac.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcelLockerService {

    Logger logger = LoggerFactory.getLogger(ParcelLockerService.class);
    @Autowired
    private ParcelLockerRepository parcelLockerRepository;

    @Autowired
    private ParcelLockerMapper parcelLockerMapper;




    public List<ParcelLockerDto> getAllParcelLocker(){
        List<ParcelLocker> parcelLockers = parcelLockerRepository.findAll();
        if(parcelLockers.isEmpty()){
            throw new EmptyListRecordException(String.format(ExceptionMessages.EMPTY_RECORDS_LIST_EXCEPTION,"Parcels Lockes"));
        }
        List<ParcelLockerDto> parcelLockersDto = parcelLockers.stream().map((parcelLocker)->this.parcelLockerMapper.mapToDto(parcelLocker)).collect(Collectors.toList());
        return parcelLockersDto;
    }
    public ParcelLockerDto getParcelLockerByID(Integer id){
        return parcelLockerMapper.mapToDto(parcelLockerRepository.findById(id).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",id))));
    }
    public ParcelLockerDto createParcelLocker(ParcelLockerDto parcelLockerDto){
        ParcelLocker parcelLocker = parcelLockerMapper.mapToEntity(parcelLockerDto);
        parcelLocker.setStatus(ParcelLockerStatus.AVAILABLE);
        ParcelLockerDto parcelLockerDtoResponse = parcelLockerMapper.mapToDto(parcelLockerRepository.save(parcelLocker));
        return parcelLockerDtoResponse;
    }
    public void deleteParcelLocker(Integer id){
        if(!parcelLockerRepository.existsById(id)){
            throw new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",id));
        }
        parcelLockerRepository.deleteById(id);
    }
    public ParcelLockerDto updateParcelLocker(ParcelLockerDto parcelLockerDto){
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerDto.getId()).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",parcelLockerDto.getId())));
        parcelLockerMapper.updateEntity(parcelLocker, parcelLockerDto);
        return parcelLockerMapper.mapToDto(parcelLockerRepository.save(parcelLocker));
    }








}
