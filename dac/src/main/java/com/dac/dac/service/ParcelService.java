package com.dac.dac.service;

import com.dac.dac.entity.InternationalParcel;
import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.Parcel;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.InternationalParcelMapper;
import com.dac.dac.mapper.LocalParcelMapper;
import com.dac.dac.mapper.ParcelMapper;
import com.dac.dac.payload.ParcelStatusDto;
import com.dac.dac.payload.response.ParcelResponseDto;
import com.dac.dac.payload.response.ParcelStaticsResponseDto;
import com.dac.dac.repository.ParcelRepository;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;
    @Autowired
    private ParcelMapper parcelMapper;
    @Autowired
    private InternationalParcelMapper internationalParcelMapper;
    @Autowired
    private LocalParcelMapper localParcelMapper;

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
    public List<ParcelStaticsResponseDto> getAllParcelStatics(){

        return parcelRepository.countParcelsByTypeStatusAndDate();

    }

}
