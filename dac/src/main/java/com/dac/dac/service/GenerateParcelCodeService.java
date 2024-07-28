package com.dac.dac.service;

import com.dac.dac.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GenerateParcelCodeService extends GenerateCodeService {

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    @Qualifier("lockerEncryptionService")
    private EncryptionService encryptionService;

    private String PARCEL_CODE_PREFIX = "RR-";
    private String TOKEN_CODE_PREFIX = "TK";
    public String generateTraceCode(){
        String code;
        do{
            Long random = generateRandomInteger(11);
            code = PARCEL_CODE_PREFIX +random;
        }while(parcelRepository.existsByTrackingCode(code));
        return code;
    }
    public String generateLockerCode() {
        String code;
        String encryptedCode = null;
        Date date = new Date();
        String currentTime = String.valueOf(date.getTime());
        String perRandom = generateRandomString(15);
        String postRandom = generateRandomString(15);
        code = perRandom+currentTime+postRandom;

        try{
            encryptedCode = encryptionService.encrypt(code);
        }catch (Exception e){
            e.printStackTrace();
        }

        return encryptedCode;

    }
    public Long generatePickupCode(){
        Long pickupCode;
        do{
            pickupCode = generateRandomInteger(6);

        }while(parcelRepository.existsByPickupCode(pickupCode));
        return pickupCode;
    }
}
