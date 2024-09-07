package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.entity.Locker;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.LockerMapper;
import com.dac.dac.payload.LockerDto;
import com.dac.dac.payload.request.LockerRequestDto;
import com.dac.dac.payload.response.LockerResponseDto;
import com.dac.dac.repository.DimensionsRepository;
import com.dac.dac.repository.LockerRepository;
import com.dac.dac.repository.ParcelLockerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LockerService {

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private ParcelLockerRepository parcelLockerRepository;

    @Autowired
    private DimensionsRepository dimensionsRepository;

    @Autowired
    private LockerMapper lockerMapper;
    Logger log = LoggerFactory.getLogger(LockerService.class);
    public List<LockerResponseDto> getAllLockers(){
        List<Locker> lockers = lockerRepository.findAll();
        List<LockerResponseDto> lockersDto = lockers.stream().map((locker)->this.lockerMapper.mapToDto(locker)).toList();
        return lockersDto;
    }
    public LockerResponseDto saveLocker(LockerRequestDto lockerDto){
        Locker locker = lockerMapper.mapToEntity(lockerDto);
        locker.setParcelLocker(parcelLockerRepository.findById(lockerDto.getParcelLockerId())
              .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",lockerDto.getParcelLockerId()))));
     locker.setLockerSize(dimensionsRepository.findById(lockerDto.getLockerSizeID())
           .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker size","id",lockerDto.getLockerSizeID()))));
        Locker lockerSaved = lockerRepository.save(locker);
        LockerResponseDto lockersDto = lockerMapper.mapToDto(lockerSaved);
        return lockersDto;
    }
    public LockerResponseDto getLockerByID(int id){
        Locker locker = lockerRepository.findById(id).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker","id",id)));
        log.info(String.valueOf(locker.getParcelLocker().getId()));
        LockerResponseDto lockersDto = lockerMapper.mapToDto(locker);
        return lockersDto;
    }
    public void deleteLocker(int id){
        if(!lockerRepository.existsById(id)){
            throw new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker","id",id));
        }
        lockerRepository.deleteById(id);
    }
    public LockerResponseDto updateLocker(LockerRequestDto lockerDto){
        Locker locker = lockerRepository.findById(lockerDto.getId()).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker","id",lockerDto.getId())));
        lockerMapper.updateEntity(lockerDto,locker);
        locker.setParcelLocker(parcelLockerRepository.findById(lockerDto.getParcelLockerId())
                .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",lockerDto.getParcelLockerId()))));
        locker.setLockerSize(dimensionsRepository.findById(lockerDto.getLockerSizeID())
                .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker size","id",lockerDto.getLockerSizeID()))));
        return lockerMapper.mapToDto(lockerRepository.save(locker));
    }
}
