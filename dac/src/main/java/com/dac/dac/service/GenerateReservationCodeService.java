package com.dac.dac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GenerateReservationCodeService extends GenerateCodeService {


     @Autowired
     @Qualifier("reservationEncryptionService")
     private EncryptionService encryptionService;
     public String generateReservationCode(){
          try{
               String plainCode = generateRandomString(15)+ LocalDateTime.now() +generateRandomString(15);
               String encrypted = encryptionService.encrypt(plainCode);
               return encrypted;
          }catch(Exception e){
              e.printStackTrace();
          }
          return null;
     }
}
