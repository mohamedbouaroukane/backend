package com.dac.dac.controller;

import com.dac.dac.payload.*;
import com.dac.dac.payload.request.CourierParcelLockerRequestDto;
import com.dac.dac.payload.response.ParcelLockerPinResponseDto;
import com.dac.dac.payload.response.ParcelLockerResponseDto;
import com.dac.dac.payload.response.ParcelLockerStaticsResponseDto;
import com.dac.dac.service.CourierParcelLockerService;
import com.dac.dac.service.ParcelLockerService;
import com.dac.dac.service.ParcelManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/parcel-locker")
public class ParcelLockerController {

    @Autowired
    private ParcelLockerService parcelLockerService;


    @Autowired
    private CourierParcelLockerService courierParcelLockerService;

    @Autowired
    private ParcelManagementService parcelManagementService;


    @GetMapping("/")
    public ResponseEntity<List<ParcelLockerResponseDto>> getAllParcelLockers(){

        return new ResponseEntity<>(parcelLockerService.getAllParcelLocker(), HttpStatus.OK);

    }
    @GetMapping("/statics")
    public ResponseEntity<List<ParcelLockerStaticsResponseDto>> getAllParcelLockersStatics(){

        return new ResponseEntity<>(courierParcelLockerService.getAllParcelLockerStatics(), HttpStatus.OK);

    }
    @GetMapping("/{id}")
    public ResponseEntity<ParcelLockerResponseDto> getParcelLockerById(@PathVariable Integer id){

        return new ResponseEntity<>(parcelLockerService.getParcelLockerByID(id), HttpStatus.OK);
    }
    

    @PostMapping("/")
    public ResponseEntity<ParcelLockerResponseDto> saveParcelLocker(@RequestBody ParcelLockerDto parcelLockerDto){
        System.out.println(parcelLockerDto.toString());
        return new ResponseEntity<>(parcelLockerService.createParcelLocker(parcelLockerDto), HttpStatus.OK);
    }
    @CrossOrigin("http://localhost:3001")
    @PutMapping("/")
    public ResponseEntity<ParcelLockerResponseDto> updateParcelLocker(@RequestBody ParcelLockerDto parcelLockerDto){

        return new ResponseEntity<>(parcelLockerService.updateParcelLocker(parcelLockerDto), HttpStatus.OK);
    }

    @CrossOrigin("http://localhost:3001")
    @DeleteMapping("/{id}")
    public void deleteParcelLocker(@PathVariable Integer id){
        parcelLockerService.deleteParcelLocker(id);
    }

    @GetMapping("/{id}/parcel")
    public ResponseEntity dropParcel(@PathVariable int id, @RequestParam String parcelCode){
        ParcelLockerPinResponseDto responseDto = parcelManagementService.dropParcel(id, parcelCode);
        Logger logger = LoggerFactory.getLogger("mohamed");
        logger.info(parcelCode);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);

    }

    @GetMapping("/{id}/parcel/{pickupCode}")
    public ResponseEntity PickupParcel(@PathVariable int id, @PathVariable Long pickupCode){
        ParcelLockerPinResponseDto responseDto = parcelManagementService.pickupParcel(id, pickupCode);
        if(responseDto != null ){
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}/access")
    public ResponseEntity courierAccess(@PathVariable int id, @RequestBody CourierParcelLockerRequestDto courierParcelLockerRequestDto){

        return new ResponseEntity(courierParcelLockerService.courierAccess(id,courierParcelLockerRequestDto.getCode()),HttpStatus.OK);
    }



}
