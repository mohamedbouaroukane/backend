package com.dac.dac.controller;


import com.dac.dac.payload.LockerDto;
import com.dac.dac.payload.request.LockerRequestDto;
import com.dac.dac.payload.response.LockerResponseDto;
import com.dac.dac.service.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locker")
public class LockerController {

    @Autowired
    private LockerService lockerService;


    @GetMapping("/")
    public ResponseEntity<List<LockerResponseDto>> getAllLockers(){
        return new ResponseEntity<>(lockerService.getAllLockers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LockerResponseDto> getLockerByID(@PathVariable int id){
        return new ResponseEntity<>(lockerService.getLockerByID(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<LockerResponseDto> saveLocker(@RequestBody LockerRequestDto lockerDto){
        return new ResponseEntity<>(lockerService.saveLocker(lockerDto), HttpStatus.CREATED);
    }
    @PutMapping("/")
    public ResponseEntity<LockerResponseDto> updateLocker(@RequestBody LockerRequestDto lockerDto){
        return new ResponseEntity<>(lockerService.updateLocker(lockerDto), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteLocker(@PathVariable int id){
        lockerService.deleteLocker(id);
    }


}
