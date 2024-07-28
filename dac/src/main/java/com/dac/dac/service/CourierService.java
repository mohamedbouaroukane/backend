package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.UserRole;
import com.dac.dac.entity.Courier;
import com.dac.dac.exption.EmptyListRecordException;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.CourierMapper;
import com.dac.dac.payload.CourierDto;
import com.dac.dac.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierService {

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private CourierRepository courierRepository;



    public List<CourierDto> getAllCourier(){
        List<Courier> couriers = courierRepository.findAll();
        if(couriers.isEmpty() ){
            throw new EmptyListRecordException(String.format(ExceptionMessages.EMPTY_RECORDS_LIST_EXCEPTION,"Couriers"));
        }
        List<CourierDto> courierDtos = couriers.stream().map((courier) -> courierMapper.mapToDto(courier)).toList();
        return courierDtos;
    }
    public CourierDto getCourierByID(Integer id){
        return courierMapper.mapToDto(courierRepository.findById(id).orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"courier","id",id))));
    }
    public CourierDto saveCourier(CourierDto courierDto){
        Courier courier = courierMapper.mapToEntity(courierDto);
        courier.setUserRole(UserRole.COURIER);
        courier.setEnabled(true);
        return courierMapper.mapToDto(courierRepository.save(courier));
    }
    public void deleteCourier(Integer id){
        if(courierRepository.existsById(id)){
            throw new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"courier","id",id));
        }
        courierRepository.deleteById(id);
    }
    public CourierDto updateCourier(CourierDto courierDto){
        Courier courier = courierRepository.findById(courierDto.getId())
                .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"courier","id",courierDto.getId())));
        courierMapper.updateEntity(courier,courierDto);
        courier.setUserRole(UserRole.COURIER);
        return courierMapper.mapToDto(courierRepository.save(courier));
    }





}
