package com.dac.dac.controller;

import com.dac.dac.payload.response.ReservationResponseDto;
import com.dac.dac.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parcel-reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{clientId}")
    List<ReservationResponseDto> getReservationByClient(@PathVariable int clientId){
        return reservationService.getReservationByClient(clientId);
    }
    @GetMapping("/")
    List<ReservationResponseDto> getAllReservation(){
        return reservationService.getAllReservation();
    }

    @DeleteMapping("/{reservationId}/client/{clientId}")
    boolean deleteReservation(@PathVariable Long reservationId, @PathVariable int clientId){
        return reservationService.deleteReservation(reservationId,clientId);
    }

}
