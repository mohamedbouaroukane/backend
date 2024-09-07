package com.dac.dac.controller;

import com.dac.dac.payload.request.ParcelRequestDto;
import com.dac.dac.payload.request.ParcelTax;
import com.dac.dac.payload.response.ParcelStaticsResponseDto;
import com.dac.dac.service.InternationalParcelService;
import com.dac.dac.service.LocalParcelService;
import com.dac.dac.service.ParcelService;
import com.dac.dac.service.ParcelStatusService;
import com.google.zxing.WriterException;
import net.sf.jasperreports.engine.JRException;
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
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/parcel")
public class ParcelController {

    Logger log = LoggerFactory.getLogger("parcel controller");
    @Autowired
    private LocalParcelService localParcelService;
    @Autowired
    private InternationalParcelService internationalParcelService;

    @Autowired
    private ParcelService parcelService;
    @Autowired
    private ParcelStatusService parcelStatusService;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity saveLocalParcel(@RequestBody ParcelRequestDto parcelRequestDto) throws JRException, IOException, WriterException {
            return new ResponseEntity(localParcelService.createLocalParcel(parcelRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/international")
    public ResponseEntity saveInternationalParcel(@RequestBody List<ParcelRequestDto> parcelRequestDto) throws JRException, IOException, WriterException {
        byte[] pdfBytes = internationalParcelService.createInternationalParcel(parcelRequestDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }

    @GetMapping("/")
    public ResponseEntity getAllParcels() {

        return new ResponseEntity(parcelService.getAllParcelsList(),HttpStatus.OK);
    }

    @GetMapping("/{traceCode}/trace")
    public ResponseEntity getTrace(@PathVariable String traceCode) {
        return new ResponseEntity(parcelService.parcelTracking(traceCode),HttpStatus.OK);
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity getParcel(@PathVariable int id) {
//        return new ResponseEntity(parcelService.getParcelByID(id),HttpStatus.OK);
//    }

    @GetMapping("/statics")
    public ResponseEntity getAllParcelStatics() {
        return new ResponseEntity(parcelService.getAllParcelStatics(),HttpStatus.OK);
    }
    @GetMapping("/status/statics")
    public ResponseEntity getAllParcelStatusStatics() {
        return new ResponseEntity(parcelService.getParcelStatusStatics(),HttpStatus.OK);
    }

    @PostMapping("/tax/over-weight")
    public ResponseEntity setParcelsOverWeightTax(@RequestBody List<ParcelTax> parcelTaxes) {
        return new ResponseEntity(parcelService.setParcelOverWeightTax(parcelTaxes),HttpStatus.OK);
    }


}
