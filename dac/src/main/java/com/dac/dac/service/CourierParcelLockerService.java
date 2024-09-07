package com.dac.dac.service;

import com.dac.dac.constants.*;
import com.dac.dac.entity.*;
import com.dac.dac.exption.EmptyListRecordException;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.exption.TokenException;
import com.dac.dac.mapper.CourierParcelLockerMapper;
import com.dac.dac.mapper.LockerMapper;
import com.dac.dac.mapper.ParcelMapper;
import com.dac.dac.payload.CourierParcelLockerDto;
import com.dac.dac.payload.response.*;
import com.dac.dac.repository.*;
import com.dac.dac.utils.CourierParcelLockerCheckerModel;
import com.dac.dac.utils.StreamUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dac.dac.utils.CustomDate.dateTostring;

@Service
public class CourierParcelLockerService {

    @Autowired
    private CourierParcelLockerRepository courierParcelLockerRepository;

    @Autowired
    private ParcelLockerRepository parcelLockerRepository;


    @Autowired
    private GenerateBarCodeService generateBarCodeService;
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private LockerMapper lockerMapper;


    @Autowired
    @Qualifier("courierEncryptionService")
    private EncryptionService encryptionService;

    @Autowired
    private StatusRepository   statusRepository;

    @Autowired
    private ParcelStatusRepository parcelStatusRepository;

    @Autowired
    private CourierParcelLockerMapper courierParcelLockerMapper;
    @Autowired
    private GeneratePDFService generatePDFService;
    @Autowired
    private ParcelRepository parcelRepository;
    @Autowired
    private ParcelMapper parcelMapper;

    @Autowired
    private EmailService emailService;
    @Autowired
    private LocalParcelRepository localParcelRepository;

    @Autowired
    private UserRepository userRepository;
    public CourierParcelLockerDto generateCourierParcelLocker(int courierId, int parcelLockerId) throws IOException, WriterException {
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerId).orElse(null);
        Courier courier = courierRepository.findById(courierId).orElse(null);

        Date nowDate = new Date();
        Date expiryDate = new Date(nowDate.getTime() + CourierAccess.COURIER_ACCESS_TIME);

        CourierParcelLocker courierParcelLocker =  CourierParcelLocker.builder()
                .courier(courier)
                .parcelLocker(parcelLocker)
                .expiryDate(expiryDate)
                .createDate(nowDate)
                .build();
        CourierParcelLockerCheckerModel courierParcelLockerChecker = CourierParcelLockerCheckerModel.builder()
                .courierId(courierId)
                .parcelLockerId(parcelLockerId)
                .expiryDate(expiryDate)
                .createDate(nowDate)
                .build();
        String code = generateCourierAccessCode(courierParcelLockerChecker);
        courierParcelLocker.setAccessCode(code);

        byte[] qrCode = StreamUtil.inputStreamToBytes(generateBarCodeService.generateQRCode(code));
        courierParcelLockerRepository.save(courierParcelLocker);
        return CourierParcelLockerDto.builder().code(qrCode).build();
    }
    public List<CourierParcelLockerResponseDto> getAllCourierAccess() {
        List<CourierParcelLocker> courierParcelLocker = courierParcelLockerRepository.findAll();
        List<CourierParcelLockerResponseDto> courierParcelLockerResponseDtos = courierParcelLockerMapper.mapToListResponseDto(courierParcelLocker);
        return courierParcelLockerResponseDtos;
    }

    @Transactional
    public CourierParcelLockerPinsResponseDto courierAccess(int parcelLockerId, String code)  {

        ObjectMapper objectMapper = new ObjectMapper();


        try {
            CourierParcelLocker courierParcelLocker = courierParcelLockerRepository.findByAccessCode(code).orElse(null);
            String decryptedCode = encryptionService.decrypt(code);
            CourierParcelLockerCheckerModel courierParcelLockerChecker = objectMapper.readValue(decryptedCode, CourierParcelLockerCheckerModel.class);

            if(isValid(courierParcelLockerChecker,parcelLockerId)){
                Status sendingStatus = statusRepository.findByStatusLabel(ParcelStatusConst.SENDER_COURIER)
                        .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","label",ParcelStatusConst.SENDER_COURIER)));
                Status returendStatus = statusRepository.findByStatusLabel(ParcelStatusConst.RETURNED)
                        .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"status","label",ParcelStatusConst.SENDER_COURIER)));

                List<Locker> expiredLockers = lockerRepository.findAllByExpiredDate(LockerStatus.NOT_AVAILABLE,parcelLockerId,ParcelStatusConst.RECEIVER_LOCKER);
                List<Locker> sendingLockers = lockerRepository.findAllByStatusAndParcelLockerIdAndParcelStatus(LockerStatus.NOT_AVAILABLE,parcelLockerId, ParcelStatusConst.SENDER_LOCKER);

                List<Parcel> parcels = lockerRepository.findAllParcelsByStatusAndCurrentParcelNotNull(LockerStatus.NOT_AVAILABLE,parcelLockerId);

                updateCollectedParcels(sendingLockers,sendingStatus);
                updateCollectedParcels(expiredLockers,returendStatus);

                User user = userRepository.findById(courierParcelLockerChecker.getCourierId()).orElse(null);
                ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerId).orElseThrow(()->new RecordNotFoundException("parcel locker not found"));
                String from = parcelLocker.getAddress().getStreet()+"("+ parcelLocker.getAddress().getZipCode()+ ")";
                byte[] manifest = generatePDFService.generateManifest(parcelMapper.mapToManifestModel(parcels),user,from,"Regional Sorting Center","Collect parcels from parcel lockers");

                List<ParcelLockerPinResponseDto> sending = lockerMapper.mapToParcelLockerResponseDto(sendingLockers);
                List<ParcelLockerPinResponseDto> expired =  lockerMapper.mapToParcelLockerResponseDto(expiredLockers);

                assert courierParcelLocker != null;
                courierParcelLocker.setUsed(true);
                courierParcelLocker.setAccessDate(new Date());
                assert user != null;
                emailService.sendManifestToCourier(user.getEmail(),manifest,"");
                courierParcelLocker.setManifest(manifest);
                return new CourierParcelLockerPinsResponseDto(sending,expired);
            }else{
                throw new TokenException("Token expired");
            }
        } catch (Exception e) {
            throw new TokenException("Cannot courier access");
        }
        

    }
    private void updateCollectedParcels(List<Locker> lockers,Status status){
        for(Locker locker : lockers){
            locker.getCurrentParcel().setStatus(status);
            locker.setStatus(LockerStatus.AVAILABLE);
            parcelStatusRepository.save(ParcelStatus.builder()
                    .parcel(locker.getCurrentParcel())
                    .status(status)
                    .date(new Date())
                    .build());
            locker.setCurrentParcel(null);

        }
    }
    private boolean isExpired (Date expiredTime) {
        return expiredTime.before(new Date());
    }
    private boolean isValid(CourierParcelLockerCheckerModel courierParcelLockerChecker,int parcelLockerId){
        ParcelLocker parcelLocker = parcelLockerRepository.findById(parcelLockerId).orElse(null);
        if(parcelLocker != null){
            return !isExpired(courierParcelLockerChecker.getExpiryDate()) && courierParcelLockerChecker.getParcelLockerId() == parcelLockerId && parcelLocker.getManager().getId() == courierParcelLockerChecker.getCourierId();
        }
        return false;
    }
    private String generateCourierAccessCode(CourierParcelLockerCheckerModel courierParcelLockerChecker){
        ObjectMapper mapper = new ObjectMapper();
        String code ="";
        try {
            code = encryptionService.encrypt(mapper.writeValueAsString(courierParcelLockerChecker));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return code;

    }

    @Transactional
    public byte[] generateAllParcelLabel(int courierId,int parcelLockerId) throws JRException, IOException, WriterException {
        List<Parcel> parcels = lockerRepository.findAllParcelsByLockerStatusAndPrintedAndParcelLockerIdAndCurrentParcelStatus(LockerStatus.NOT_AVAILABLE,false,parcelLockerId, ParcelStatusConst.SENDER_LOCKER);
        System.out.println(parcels.size());
        if(!parcels.isEmpty()){

            parcels.forEach((parcel)-> {
                parcel.setIsPrinted(true);
                parcelRepository.save(parcel);

            });
            return generatePDFService.generateMultiParcelLabel(parcels);
        }

       throw new EmptyListRecordException(String.format(ExceptionMessages.EMPTY_RECORDS_LIST_EXCEPTION,"parcels"));
    }

    public byte[] generateAllParcelLabel() throws JRException, IOException, WriterException {
        return generatePDFService.generateMultiParcelLabel(parcelRepository.findAll());
    }
    public List<CourierParcelLockerResponseDto> getCourierParcelLockerAccessHistory() {
        return courierParcelLockerMapper.mapToListResponseDto( courierParcelLockerRepository.findAll());
    }

    public List<ParcelLockerStaticsResponseDto> getAllParcelLockerStatics(){
        List<ParcelLocker> parcelLockers = parcelLockerRepository.findAll();
        List<ParcelLockerStaticsResponseDto> parcelLockerStaticsResponseDtos = new ArrayList<>();
        for(ParcelLocker parcelLocker : parcelLockers){
            long notAvailableLockers = lockerRepository.countByStatusAndParcelLockerId(LockerStatus.NOT_AVAILABLE,parcelLocker.getId());
            long allLockers = parcelLocker.getLockers().size();
            double usagePercentage = ((double) (notAvailableLockers) /(allLockers))*100;
            ParcelLockerStaticsResponseDto parcelLockerStaticsResponseDto =ParcelLockerStaticsResponseDto.builder().
                    id(parcelLocker.getId()).
                    parcelLockerUsagePercentage(usagePercentage).
                    parcelLockerName(parcelLocker.getName()).
                    parcelLockerAddress(parcelLocker.getAddress().getStreet() +" ("+parcelLocker.getAddress().getZipCode() +")").

                    build();
            CourierParcelLocker courierParcelLocker = courierParcelLockerRepository.findFirstByParcelLockerIdOrderByCreateDateDesc(parcelLocker.getId()).orElse(null);

            if(courierParcelLocker != null){
                parcelLockerStaticsResponseDto.setCourierLastTimeAccessId(courierParcelLocker.getCourier().getId());
                parcelLockerStaticsResponseDto.setCourierLastTimeAccessName(courierParcelLocker.getCourier().getFullName());
                parcelLockerStaticsResponseDto.setLastTimeAccess(courierParcelLocker.getAccessDate() != null ?dateTostring(courierParcelLocker.getAccessDate()) : "-" );
            }
            parcelLockerStaticsResponseDtos.add(parcelLockerStaticsResponseDto);
        }
        return parcelLockerStaticsResponseDtos;
    }
    public List<ParcelLockerStaticsCourierResponseDto> getParcelLockerStaticsCourier() {

            List<ParcelLockerStaticsCourierResponseDto> parcelLockerResponse = new ArrayList();
            List<ParcelLocker> parcelLockers = parcelLockerRepository.findAll();
            for(ParcelLocker parcelLocker : parcelLockers){
                long sendingParcels = lockerRepository.countByParcelStatusAndParcelLockerId(ParcelStatusConst.SENDER_LOCKER,parcelLocker.getId());
                if(sendingParcels > 0){
                    long receivingParcels = lockerRepository.countByParcelStatusAndParcelLockerId(ParcelStatusConst.RECEIVER_LOCKER,parcelLocker.getId());
                    long parcelPrintedNumber = lockerRepository.countByParcelIsPrintedAndParcelLockerId(false,parcelLocker.getId());
                    long notAvailableLockers = lockerRepository.countByStatusAndParcelLockerId(LockerStatus.NOT_AVAILABLE,parcelLocker.getId());
                    long allLockers = parcelLocker.getLockers().size();
                    System.out.println(notAvailableLockers +"- "+allLockers);
                    System.out.println((double)(3/15));
                    double usagePercentage = ((double) (notAvailableLockers) /(allLockers))*100;
                    double notPrintedPercentage = ((double) parcelPrintedNumber /sendingParcels)*100 ;

                    parcelLockerResponse.add(
                            ParcelLockerStaticsCourierResponseDto
                                    .builder()
                                    .receivingParcel(receivingParcels)
                                    .sendingParcels(sendingParcels)
                                    .parcelLockerNotPrintedPercentage(notPrintedPercentage)
                                    .parcelLockerUsagePercentage(usagePercentage)
                                    .parcelLockerPriority(getPriority(usagePercentage,notPrintedPercentage))
                                    .parcelPrintedNumber(parcelPrintedNumber)
                                    .parcelLockerAddress(parcelLocker.getAddress().toString())
                                    .parcelLockerLatitude(parcelLocker.getAddress().getLatitude())
                                    .parcelLockerLongitude(parcelLocker.getAddress().getLongitude())
                                    .parcelLockerName(parcelLocker.getName())
                                    .id(parcelLocker.getId())
                                    .build()
                    );
                }
            }


            return parcelLockerResponse;
    }
    private Priority getPriority(double usagePercentage,double notPrintedPercentage){

        if(notPrintedPercentage > 60 || usagePercentage > 60){
            return Priority.HIGH;
        } else if ((usagePercentage < 60 && usagePercentage > 30) ||(notPrintedPercentage < 60 && notPrintedPercentage > 30)) {
            return Priority.MEDIUM;
        }else{
            return Priority.LOW;
        }
    }
    public List<CourierAccessResponseDto> getCourierAccessHistory(){
        return courierParcelLockerRepository.findFirstTenRecords(PageRequest.of(0, 10));
    }

    public byte[] sendReports(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -24);
        Date cutoffDate = cal.getTime();
        System.out.println(cutoffDate);
        List<Courier> couriers = courierParcelLockerRepository.findCouriersNotAccessedSince(cutoffDate);

        return generatePDFService.generateCourierReport();
    }

    public byte[] getCourierAccessById(int id){
        CourierParcelLocker courierParcelLocker =courierParcelLockerRepository.findById(id).orElseThrow(()->new RecordNotFoundException("courier access id not found"));
        return courierParcelLocker.getManifest();
    }

}
