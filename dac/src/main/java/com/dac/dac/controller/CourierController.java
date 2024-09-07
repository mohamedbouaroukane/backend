package com.dac.dac.controller;

import com.dac.dac.payload.CourierDto;
import com.dac.dac.payload.CourierParcelLockerDto;
import com.dac.dac.payload.response.CourierParcelLockerResponseDto;
import com.dac.dac.payload.response.ParcelLockerStaticsCourierResponseDto;
import com.dac.dac.service.CourierParcelLockerService;
import com.dac.dac.service.CourierService;
import com.dac.dac.service.GeneratePDFService;
import com.dac.dac.service.ParcelService;
import com.dac.dac.utils.CustomDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private CourierParcelLockerService courierParcelLockerService;
    @Autowired
    private ParcelService parcelService;


    @GetMapping("/")
    private List<CourierDto> getAllCourier(){
        return courierService.getAllCourier();
    }

    @GetMapping("/{id}")
    private CourierDto getCourierByID(@PathVariable Integer id){
        return courierService.getCourierByID(id);
    }

    @PostMapping("/")
    private CourierDto saveCourier(@RequestBody CourierDto courier){
        return courierService.saveCourier(courier);
    }

    @PutMapping("/")
    private CourierDto updateCourier(@RequestBody CourierDto courier){
        return courierService.updateCourier(courier);
    }

    @DeleteMapping("/{id}")
    private void deleteCourier(@PathVariable Integer id){
        courierService.deleteCourier(id);
    }


    @GetMapping("/{id}/parcel-locker/{parcelLockerId}")
    public CourierParcelLockerDto lockerCourier(@PathVariable Integer id, @PathVariable Integer parcelLockerId){
        try{
            return  courierParcelLockerService.generateCourierParcelLocker(id, parcelLockerId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping("/{id}/parcel-locker/history")
    public List<CourierParcelLockerResponseDto> courierParcelLockerAccessHistory(@PathVariable Integer id){
        try{
            return  courierParcelLockerService.getCourierParcelLockerAccessHistory();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Autowired
    private GeneratePDFService generatePDFService;

    @CrossOrigin("**")
    @GetMapping("{id}/parcel-locker/{parcelLockerId}/parcel-label")
    public ResponseEntity getPdf(@PathVariable int id,@PathVariable int parcelLockerId) throws Exception {
        byte[] pdfBytes = courierParcelLockerService.generateAllParcelLabel(id,parcelLockerId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }

    @GetMapping("/parcel-locker/statics")
    public ResponseEntity getStatics() throws Exception {

        List<ParcelLockerStaticsCourierResponseDto> parcelLockerStaticsCourierResponseDtos = courierParcelLockerService.getParcelLockerStaticsCourier();
       parcelLockerStaticsCourierResponseDtos.sort(Comparator.comparingDouble(ParcelLockerStaticsCourierResponseDto::getParcelPrintedNumber).reversed());
        return  new ResponseEntity( parcelLockerStaticsCourierResponseDtos, HttpStatus.OK);
    }
    @GetMapping("/{id}/account")
    public ResponseEntity getAccount(@PathVariable Integer id) throws Exception {
        return new ResponseEntity(courierService.getCourierAccountById(id),HttpStatus.OK);
    }

    @GetMapping("/access/statics")
    public ResponseEntity getCourierAccessStatics() throws Exception {
        return new ResponseEntity(courierParcelLockerService.getCourierAccessHistory(),HttpStatus.OK);
    }

    @GetMapping("/access/")
    public ResponseEntity getCourierAccessList() throws Exception {
        return new ResponseEntity(courierParcelLockerService.getAllCourierAccess(),HttpStatus.OK);
    }
    @GetMapping("/reports/")
    public ResponseEntity sendReports() throws Exception {
        byte[] pdfBytes = courierParcelLockerService.sendReports();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }
    @GetMapping("/{id}/parcel/payded")
    public ResponseEntity getAllParcelStatusStatics(@PathVariable Integer id) throws Exception {
        return new ResponseEntity(parcelService.getAllCourierPayedParcels(id),HttpStatus.OK);
    }
    @GetMapping("/access/{id}")
    public ResponseEntity<InputStreamResource> getCourierAccessById(@PathVariable int id){
        byte[] pdfBytes = courierParcelLockerService.getCourierAccessById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=access manifest"+ id +".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }
}
