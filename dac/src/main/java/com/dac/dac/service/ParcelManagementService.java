package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.*;
import com.dac.dac.exption.EmptyListRecordException;
import com.dac.dac.exption.ParcelDropException;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.ParcelMapper;
import com.dac.dac.payload.response.ParcelLockerResponseDto;
import com.dac.dac.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ParcelManagementService {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ParcelStatusService parcelStatusService;

    @Autowired
    private GenerateParcelCodeService generateParcelCodeService;

    @Autowired
    private ParcelLockerRepository parcelLockerRepository;

    @Autowired
    private GeneratePDFService generatePDFService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParcelMapper parcelMapper;

    public ParcelLockerResponseDto dropParcel(int parcelLockerId, String parcelCode) {
        Logger logger = LoggerFactory.getLogger("mohamed");
        logger.info(parcelCode);
        Parcel parcel = parcelRepository.findByLockerCode(parcelCode).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel","locker code",parcelCode)));
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerId).orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"Parcel locker","id",parcelLockerId)));


        if(parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.CREATED) || (parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.RECEIVER_COURIER) && parcel.getReceiverParcelLocker().getId() == parcelLockerId) ){
            Locker locker = lockerRepository.findFirstByStatusAndLockerSizeIdAndParcelLockerId(LockerStatus.AVAILABLE,parcel.getParcelDetail().getDimensions().getId(),parcelLocker.getId()).orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker","multi fields",null)));
            if(locker != null){
                Status status;
                if(parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.CREATED)){
                    LocalParcel localParcel = (LocalParcel) parcel;
                    status = statusRepository.findByStatusLabel(ParcelStatusConst.SENDER_LOCKER).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","status label",0)));
                    localParcel.setSenderLocker(locker);
                }else{
                    status = statusRepository.findByStatusLabel(ParcelStatusConst.RECEIVER_LOCKER).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","status label",0)));
                    parcel.setReceiverLocker(locker);
                    parcel.setPickupCode(generateParcelCodeService.generatePickupCode());
                    parcel.setReceiverDate(new Date((new Date()).getTime() + 50000));
                    // TODO: send to receiver a sms with pickup code
                }

                locker.setStatus(LockerStatus.NOT_AVAILABLE);
                parcel.setStatus(status);
                locker.setCurrentParcel(parcel);

                parcelStatusService.logParcelStatus(parcel,status);

                return ParcelLockerResponseDto.builder().pin(locker.getPin()).build();
            }
        }
        throw new ParcelDropException("Cannot drop Parcel");
    }


    public ParcelLockerResponseDto pickupParcel(int parcelLockerId,Long pickupCode){
        Parcel parcel = parcelRepository.findByPickupCode(pickupCode).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"parcel","pickup number",pickupCode)));
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerId).orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"Parcel locker","id",parcelLockerId)));
        if(parcel.getReceiverParcelLocker().getId() == parcelLocker.getId() && parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.RECEIVER_LOCKER) ){
            Status status = statusRepository.findByStatusLabel(ParcelStatusConst.DELIVERED).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","status label",0)));;
            parcel.getReceiverLocker().setStatus(LockerStatus.AVAILABLE);
            parcel.getReceiverLocker().setCurrentParcel(null);
            parcel.setStatus(status);
            return ParcelLockerResponseDto.builder().pin(parcel.getReceiverLocker().getPin()).build();
        }

        return null;
    }


    public byte[] entryParcelToRSC(int userId,List<String> traceCodes) {
        List<Parcel> parcels = parcelRepository.findAllByTrackingCodeIn(traceCodes);
        Status status = statusRepository.findByStatusLabel(ParcelStatusConst.CTR_HUB)
                .orElseThrow(() -> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION, "status", "label", ParcelStatusConst.CTR_HUB)));
        if(parcels.isEmpty()){
            throw new EmptyListRecordException("Empty list record");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"user","id",userId)));

        for (Parcel parcel : parcels) {
            if(parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.SENDER_COURIER)){
                parcel.setStatus(status);
                parcelStatusService.logParcelStatus(parcel, status);
            }

        }

        return generatePDFService.generateManifest(parcelMapper.mapToManifestModel(parcels),user,"Regional Sorting Center",parcels.get(0).getReceiverParcelLocker().getAddress().toString(),"Entry Parcel To CTR");

    }

    public byte[] exitParcelFromRSC(int userId,List<String> traceCodes) {
        List<Parcel> parcels = parcelRepository.findAllByTrackingCodeIn(traceCodes);
        Status status = statusRepository.findByStatusLabel(ParcelStatusConst.RECEIVER_COURIER)
                .orElseThrow(() -> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION, "status", "label", ParcelStatusConst.CTR_HUB)));
        User user = userRepository.findById(userId).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"user","id",userId)));
        for (Parcel parcel : parcels) {
            if(parcel.getStatus().getStatusLabel().equals(ParcelStatusConst.CTR_HUB)){
                parcel.setStatus(status);
                parcelStatusService.logParcelStatus(parcel, status);
            }

        }
        return generatePDFService.generateManifest(parcelMapper.mapToManifestModel(parcels),user,"Regional Sorting Center",parcels.get(0).getReceiverParcelLocker().getAddress().toString(),"Send Parcels to parcel Locker");

    }


}
