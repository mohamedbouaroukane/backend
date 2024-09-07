package com.dac.dac.service;

import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.*;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.InternationalParcelMapper;
import com.dac.dac.mapper.LocalParcelMapper;
import com.dac.dac.mapper.ParcelMapper;
import com.dac.dac.payload.ParcelStatusDto;
import com.dac.dac.payload.request.ParcelTax;
import com.dac.dac.payload.response.*;
import com.dac.dac.repository.ParcelRepository;
import com.dac.dac.repository.ParcelStatusRepository;
import com.dac.dac.repository.StatusRepository;
import com.dac.dac.utils.ParcelTaxUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;
    @Autowired
    private ParcelMapper parcelMapper;
    @Autowired
    private ParcelStatusRepository parcelStatusRepository;
    @Autowired
    private InternationalParcelMapper internationalParcelMapper;
    @Autowired
    private LocalParcelMapper localParcelMapper;
    @Autowired
    private StatusRepository statusRepository;

    public List<ParcelStatusDto> parcelTracking(String traceCode){
        Parcel parcel = parcelRepository.findByTrackingCode(traceCode).orElseThrow(()->new RecordNotFoundException("The parcel with trace code : "+traceCode+" Not Found"));
        return parcelMapper.mapToDtoListStatus(parcel);
    }

    public ParcelResponseDto getAllParcels(){
        List<Parcel> parcels = parcelRepository.findAll();
        ParcelResponseDto responseDto = new ParcelResponseDto();
        for(Parcel parcel : parcels){
            if(parcel instanceof InternationalParcel internationalParcel) {
                responseDto.getInternationalParcels().add(internationalParcelMapper.mapToDto(internationalParcel));
            }else if(parcel instanceof LocalParcel localParcel){
                responseDto.getLocalParcels().add(localParcelMapper.mapToDto(localParcel));
            }
        }
        return responseDto;
    }

    public List<ParcelListResponseDto> getAllParcelsList(){
        List<Parcel> parcels = parcelRepository.findAll();
        List<ParcelListResponseDto> responseDtos = parcelMapper.mapToParcelListResponseDto(parcels);
        return responseDtos;
    }
    public List<ParcelStaticsResponseDto> getAllParcelStatics(){

        return parcelRepository.countParcelsByTypeStatusAndDate();

    }
    public List<ParcelStatusStaticsResponseDto>  getParcelStatusStatics(){
        List<ParcelStatusStaticsResponseDto> results = parcelRepository.findAllParcelStatus();

        Map<ParcelStatusConst, Long> statusMap = results.stream()
                .collect(Collectors.toMap(ParcelStatusStaticsResponseDto::getStatus, ParcelStatusStaticsResponseDto::getNumberOfParcels));

        for (ParcelStatusConst status : ParcelStatusConst.values()) {
            statusMap.putIfAbsent(status, 0L);
        }
        return statusMap.entrySet().stream()
                .map(entry -> new ParcelStatusStaticsResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean setParcelOverWeightTax(List<ParcelTax> parcelTaxList){
        Status status = statusRepository.findByStatusLabel(ParcelStatusConst.OVER_WEIGHT).orElseThrow(()->new RecordNotFoundException("parcel status not found" ));
        for(ParcelTax parcelTax : parcelTaxList){
            Parcel parcel = parcelRepository.findByTrackingCode(parcelTax.getParcelTrackingNumber()).orElse(null);
            if(parcel instanceof LocalParcel localParcel){
               if(localParcel.getStatus().getStatusLabel().equals(ParcelStatusConst.SENDER_COURIER)){
                   localParcel.setTaxAmount(ParcelTaxUtils.ParcelTaxCalculator(parcelTax.getWeight()));
                   localParcel.setStatus(status);
                   localParcel.setTaxFilingDate(LocalDateTime.now());
                   parcelStatusRepository.save(ParcelStatus.builder().parcel(parcel).status(status).date(new Date()).build());
               }

            }
        }
        return true;

    }
    public List<CourierPayedParcelResponse> getAllCourierPayedParcels(int courierId){
            return parcelRepository.findPaidParcelsInLocker(ParcelStatusConst.PAYDED, courierId);
    }

}
