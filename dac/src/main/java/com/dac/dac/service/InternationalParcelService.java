package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.*;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.InternationalParcelMapper;
import com.dac.dac.mapper.LocalParcelMapper;
import com.dac.dac.mapper.ParcelMapper;
import com.dac.dac.payload.request.ParcelRequestDto;
import com.dac.dac.payload.response.InternationalParcelResponseDto;
import com.dac.dac.payload.response.LocalParcelResponseDto;
import com.dac.dac.repository.*;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class InternationalParcelService {
    @Autowired
    private InternationalParcelRepository parcelRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ParcelDetailRepository parcelDetailRepository;
    @Autowired
    private ParcelLockerRepository parcelLockerRepository;


    @Autowired
    private ParcelMapper parcelMapper;

    @Autowired
    private ParcelStatusRepository parcelStatusRepository;

    @Autowired
    private GenerateParcelCodeService generateParcelCodeService;

    @Autowired
    private ParcelStatusService parcelStatusService;
    @Autowired
    private GeneratePDFService generatePDFService;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LocalParcelRepository localParcelRepository;

    @Autowired
    private LocalParcelMapper localParcelMapper;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private InternationalParcelMapper internationalParcelMapper;
    Logger logger = LoggerFactory.getLogger(ParcelLockerService.class);
    @Autowired
    private InternationalParcelRepository internationalParcelRepository;

    public byte[] createInternationalParcel(List<ParcelRequestDto> parcelRequestDtos) throws JRException, IOException, WriterException {
        List<Parcel> parcels = new ArrayList<>();
        for(ParcelRequestDto parcelRequestDto : parcelRequestDtos){
            parcels.add(createInternationalParcel(parcelRequestDto));
        }
       return generatePDFService.generateMultiParcelLabel(parcels);
    }

    @Transactional
    public Parcel createInternationalParcel(ParcelRequestDto parcelRequestDto) throws JRException, IOException, WriterException {
        InternationalParcel parcel = internationalParcelMapper.mapToEntity(parcelRequestDto);
        String trackCode = generateParcelCodeService.generateTraceCode();
        String lockerCode = generateParcelCodeService.generateLockerCode();

        ParcelDetail parcelDetail = parcelDetailRepository.findByDimensionsId(parcelRequestDto.getParcelTypeId())
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel details","id",parcelRequestDto.getParcelTypeId())));
        Status status = statusRepository.findByStatusLabel(ParcelStatusConst.RECEIVER_COURIER)
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","label",ParcelStatusConst.CREATED)));
        ParcelLocker pl = parcelLockerRepository.findById(1).orElse(null);
        parcel.setReceiverParcelLocker(pl);
        parcel.setParcelDetail(parcelDetail);
        parcel.setLockerCode(lockerCode);
        parcel.setTrackingCode(trackCode);
        parcel.setStatus(status);


        internationalParcelRepository.save(parcel);
        parcelStatusRepository.save(ParcelStatus.builder().parcel(parcel).status(status).date(new Date()).build());
        return parcel;

    }
    public Optional<ParcelLocker> getNearestParcelLockerByZipCode(int zipCode) {
        Optional<Address> address = addressRepository.findById(zipCode);

        if (address.isEmpty()) {
            return Optional.empty();
        }

        List<ParcelLocker> allParcelLockers = parcelLockerRepository.findAll();

        return allParcelLockers.stream()
                .min(Comparator.comparingDouble(locker -> calculateDistance(address.get(), locker)));
    }

    private double calculateDistance(Address address1, ParcelLocker locker) {
        // Implement Haversine formula here
        // For this, you'll need latitude and longitude of the address
        // You might need to add these fields to your Address entity
        // or use a geocoding service to get coordinates from zip code

        double lat1 = address1.getLatitude();
        double lon1 = address1.getLongitude();
        double lat2 = locker.getAddress().getLatitude();
        double lon2 = locker.getAddress().getLongitude();

        double earthRadius = 6371; // in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
    public void getAllParcelStatics(){
        List<Object[]> results = parcelRepository.countParcelsByTypeStatusAndDate();
        for(Object[] result : results){
            System.out.println(result[0] +"- " + result[1]); ;
        }

    }

}
