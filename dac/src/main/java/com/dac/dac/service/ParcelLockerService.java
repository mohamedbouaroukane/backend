package com.dac.dac.service;

import com.dac.dac.constants.*;
import com.dac.dac.entity.*;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.ParcelLockerMapper;
import com.dac.dac.payload.ParcelLockerDto;
import com.dac.dac.payload.response.ParcelLockerDetailResponseDto;
import com.dac.dac.payload.response.ParcelLockerResponseDto;
import com.dac.dac.payload.response.ParcelLockerStaticsResponseDto;
import com.dac.dac.repository.*;
import jakarta.transaction.Transactional;
import jdk.jfr.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dac.dac.utils.CustomDate.dateTostring;

@Service
public class ParcelLockerService {

    Logger logger = LoggerFactory.getLogger(ParcelLockerService.class);
    @Autowired
    private ParcelLockerRepository parcelLockerRepository;

    @Autowired
    private ParcelLockerMapper parcelLockerMapper;

    @Autowired
    private CourierParcelLockerRepository courierParcelLockerRepository;

    @Autowired
    private LockerRepository lockerRepository;



    public List<ParcelLockerResponseDto> getAllParcelLocker(){
        List<ParcelLocker> parcelLockers = parcelLockerRepository.findAll();
        List<ParcelLockerResponseDto> parcelLockersDto = new ArrayList<>();
        for(ParcelLocker parcelLocker : parcelLockers){
            ParcelLockerResponseDto parcelLockerResponseDto = parcelLockerMapper.mapToDto(parcelLocker);
            long notAvailableLockers = lockerRepository.countByStatusAndParcelLockerId(LockerStatus.NOT_AVAILABLE,parcelLocker.getId());
            long allLockers = parcelLocker.getLockers().size();
            double usagePercentage = ((double) (notAvailableLockers) /(allLockers))*100;
            parcelLockerResponseDto.setUsagePercentage(usagePercentage);
            parcelLockerResponseDto.setPriority(getPriority(usagePercentage,allLockers));
            parcelLockersDto.add(parcelLockerResponseDto);
        }
        return parcelLockersDto;
    }
    public ParcelLockerResponseDto getParcelLockerByID(Integer id){
        ParcelLocker parcelLocker = parcelLockerRepository.findById(id).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",id)));
        ParcelLockerDetailResponseDto ParcelLockerDetailResponseDto =  parcelLockerMapper.mapToDetailedDto(parcelLocker);
        long notAvailableLockers = lockerRepository.countByStatusAndParcelLockerId(LockerStatus.NOT_AVAILABLE,parcelLocker.getId());
        long allLockers = parcelLocker.getLockers().size();
        double usagePercentage = ((double) (notAvailableLockers) /(allLockers))*100;
        ParcelLockerDetailResponseDto.setUsagePercentage(usagePercentage);
        ParcelLockerDetailResponseDto.setPriority(getPriority(usagePercentage,allLockers));
        CourierParcelLocker courierParcelLocker = courierParcelLockerRepository.findFirstByParcelLockerIdOrderByCreateDateDesc(parcelLocker.getId()).orElse(null);

        if(courierParcelLocker != null){
            ParcelLockerDetailResponseDto.setAccessCourier(courierParcelLocker.getCourier());
            ParcelLockerDetailResponseDto.setLastTime(dateTostring(courierParcelLocker.getAccessDate()));
        }

        return ParcelLockerDetailResponseDto;
    }
    @Transactional
    public ParcelLockerResponseDto createParcelLocker(ParcelLockerDto parcelLockerDto){
        ParcelLocker parcelLocker = parcelLockerMapper.mapToEntity(parcelLockerDto);
        ParcelLocker savedParcelLocker = parcelLockerRepository.save(parcelLocker);
        ParcelLockerResponseDto parcelLockerDtoResponse = parcelLockerMapper.mapToDto(savedParcelLocker);
        for(Locker locker:parcelLockerDto.getLockers()){
            locker.setParcelLocker(savedParcelLocker);
        }
        return parcelLockerDtoResponse;
    }
    public void deleteParcelLocker(Integer id){
        if(!parcelLockerRepository.existsById(id)){
            throw new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",id));
        }
        parcelLockerRepository.deleteById(id);
    }
    public ParcelLockerResponseDto updateParcelLocker(ParcelLockerDto parcelLockerDto){
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerDto.getId()).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel locker","id",parcelLockerDto.getId())));
        parcelLockerMapper.updateEntity(parcelLocker, parcelLockerDto);
        return parcelLockerMapper.mapToDto(parcelLockerRepository.save(parcelLocker));
    }
    private Priority getPriority(double usagePercentage,double allLockers){

        if(allLockers > 60 || usagePercentage > 60){
            return Priority.HIGH;
        } else if ((usagePercentage < 60 && usagePercentage > 30) ||(allLockers < 60 && allLockers > 30)) {
            return Priority.MEDIUM;
        }else{
            return Priority.LOW;
        }
    }








}
