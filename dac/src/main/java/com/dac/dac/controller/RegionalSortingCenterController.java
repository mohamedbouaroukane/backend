package com.dac.dac.controller;

import com.dac.dac.payload.request.RSCRequestDto;
import com.dac.dac.service.ParcelManagementService;
import com.dac.dac.utils.CustomDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/rsc")
public class RegionalSortingCenterController {

    @Autowired
    private ParcelManagementService parcelManagementService;



    @CrossOrigin("*")
    @PostMapping("/entry")
    public ResponseEntity<InputStreamResource> entryParcels(@RequestBody RSCRequestDto rscRequestDto){
        byte[] pdfBytes = parcelManagementService.entryParcelToRSC(rscRequestDto.getId(),rscRequestDto.getTraceCodes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=entry manifest"+ CustomDate.dateTostring(new Date()) +".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }

    @PostMapping("/exit")
    public ResponseEntity<InputStreamResource> exitParcels(@RequestBody RSCRequestDto rscRequestDto){

        byte[] pdfBytes = parcelManagementService.exitParcelFromRSC(rscRequestDto.getId(),rscRequestDto.getTraceCodes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exit manifest"+ CustomDate.dateTostring(new Date()) +".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));

    }
}
