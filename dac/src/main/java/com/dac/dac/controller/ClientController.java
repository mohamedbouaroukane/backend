package com.dac.dac.controller;

import com.dac.dac.payload.ClientDto;
import com.dac.dac.payload.response.LocalParcelResponseDto;
import com.dac.dac.service.ClientService;
import com.dac.dac.service.ParcelManagementService;
import com.dac.dac.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ParcelService parcelService;
    @Autowired
    private ParcelManagementService parcelManagementService;

    @GetMapping("/")
    ResponseEntity<List<ClientDto>> getAllClient(){
        return new ResponseEntity<List<ClientDto>>(clientService.getAllClients(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<ClientDto> getClientByID(@PathVariable Integer id){

        return new ResponseEntity<ClientDto>(clientService.getClientByID(id), HttpStatus.OK);
    }
    @PostMapping("/")
    ResponseEntity<ClientDto> saveClient(@RequestBody ClientDto clientDto){
        return new ResponseEntity<ClientDto>(clientService.saveClient(clientDto),HttpStatus.CREATED);
    }
    @PutMapping("/")
    ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto){
        return new ResponseEntity<ClientDto>(clientService.updateClient(clientDto),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    void deleteClient(@PathVariable Integer id){
        clientService.deleteClient(id);
    }

//    @GetMapping("/{id}/send-parcels")
//    ResponseEntity<List<LocalParcelResponseDto>> sendParcels(@PathVariable Integer id){
//       return new ResponseEntity<>(parcelService.getSendingParcels(id),HttpStatus.OK);
//    }
//
//
//    @GetMapping("/{id}/receive-parcels")
//    @PreAuthorize("hasRole('CLIENT')")
//    ResponseEntity<List<LocalParcelResponseDto>> receiveParcels(@PathVariable Integer id){
//        return new ResponseEntity<>(parcelService.getReceivingParcels(id),HttpStatus.OK);
//    }

    @PostMapping("/{id}/parcel/{parcelId}/pay")
    void payParcelTax(@PathVariable Integer id,@PathVariable Integer parcelId){
        parcelManagementService.payedTaxParcel(parcelId,id);
    }
}
