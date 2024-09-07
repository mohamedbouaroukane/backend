package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.*;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.LocalParcelMapper;
import com.dac.dac.payload.request.ParcelRequestDto;
import com.dac.dac.payload.response.LocalParcelResponseDto;
import com.dac.dac.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocalParcelService {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ParcelDetailRepository parcelDetailRepository;

    @Autowired
    private ParcelStatusRepository parcelStatusRepository;

    @Autowired
    private GenerateParcelCodeService generateParcelCodeService;

    @Autowired
    private GeneratePDFService generatePDFService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LocalParcelRepository localParcelRepository;

    @Autowired
    private LocalParcelMapper localParcelMapper;

    @Autowired
    private ReservationService reservationService;
    Logger logger = LoggerFactory.getLogger(ParcelLockerService.class);


    @Transactional
    public LocalParcelResponseDto createLocalParcel(ParcelRequestDto parcelRequestDto){
        LocalParcel parcel = localParcelMapper.mapToEntity(parcelRequestDto);
        String trackCode = generateParcelCodeService.generateTraceCode();
        String lockerCode = generateParcelCodeService.generateLockerCode();

        ParcelDetail parcelDetail = parcelDetailRepository.findByDimensionsId(parcelRequestDto.getParcelTypeId())
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel details","id",parcelRequestDto.getParcelTypeId())));
        Status status = statusRepository.findByStatusLabel(ParcelStatusConst.CREATED)
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","label",ParcelStatusConst.CREATED)));
        Client client = clientRepository.findById(parcelRequestDto.getSenderId())
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",parcelRequestDto.getSenderId())));

        parcel.setParcelDetail(parcelDetail);
        parcel.setLockerCode(lockerCode);
        parcel.setTrackingCode(trackCode);
        parcel.setStatus(status);
        parcel.setSender(client);

        LocalParcelResponseDto parcelResponseDto =null;
        try{
            LocalParcel parcelSaved = localParcelRepository.save(parcel);
            parcelResponseDto = localParcelMapper.mapToDto(parcelSaved);
            parcelStatusRepository.save(ParcelStatus.builder().parcel(parcel).status(status).date(new Date()).build());
            if(parcelRequestDto.isWithLabel()){
                parcelSaved.setIsPrinted(false);

                emailService.sendParcelLabelAttachmentsEmail(client.getEmail(),client.getFullName(),generatePDFService.generateParcelLabel(parcel));
            }
            if(parcelRequestDto.isWithReservation()){
                parcelRequestDto.getReservationRequestDto().setParcelId(parcelSaved.getId());
                parcelRequestDto.getReservationRequestDto().setParcelSize(parcelRequestDto.getParcelTypeId());
                if(reservationService.createReservation(parcelRequestDto.getReservationRequestDto()) == null){
                    logger.info("can not create a reservation");
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }


        return parcelResponseDto;
    }
//
//    public List<LocalParcelResponseDto> getAllParcels(){
//        List<Parcel> parcels = parcelRepository.findAll();
//        if(parcels.isEmpty()){
//            throw new EmptyListRecordException(String.format(ExceptionMessages.EMPTY_RECORDS_LIST_EXCEPTION,"parcels"));
//        }
//        return parcelMapper.mapToDto(parcels);
//    }
//    public List<ParcelStatusDto> parcelTrace(String traceCode){
//        Parcel parcel = parcelRepository.findByTraceCode(traceCode).orElseThrow(()->new RecordNotFoundException("The parcel with trace code : "+traceCode+" Not Found"));
//
//        return parcelMapper.mapToDtoListStatus(parcel);
//    }
//
//    public LocalParcelResponseDto getParcelByID(int id){
//        return parcelMapper.mapToDto(parcelRepository.findById(id).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel","id",id))));
//    }
//
//    public List<LocalParcelResponseDto> getSendingParcels(int senderId){
//        return parcelMapper.mapToDto(parcelRepository.findAllBySenderId(senderId));
//    }
//    public List<LocalParcelResponseDto> getReceivingParcels(int receiverId){
//        logger.info(String.valueOf(receiverId));
//        Client client = clientRepository.findById(receiverId)
//                .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",receiverId)));
//
//
//        return parcelMapper.mapToDto( parcelRepository.findAllByReceiverPhone(client.getPhone()));
//    }
}
