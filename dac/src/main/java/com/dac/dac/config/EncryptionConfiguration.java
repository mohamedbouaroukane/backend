package com.dac.dac.config;

import com.dac.dac.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfiguration {

    @Value("${encry.secretKey.courier}")
    private String secretKeyCourierString;

    @Value("${encry.secretKey.token}")
    private String secretKeyTokenString;

    @Value("${encry.secretKey.locker}")
    private String secretKeyClientLockerString;

    @Value("${encry.secretKey.reservation}")
    private String secretKeyReservationString;

    @Bean(name = "courierEncryptionService")
    public EncryptionService courierEncryptionService() throws Exception {
        return new EncryptionService(secretKeyCourierString);
    }

    @Bean(name = "tokenEncryptionService")
    public EncryptionService tokenEncryptionService() throws Exception {
        return new EncryptionService(secretKeyTokenString);
    }
    @Bean(name = "lockerEncryptionService")
    public EncryptionService lockerEncryptionService() throws Exception {
        return new EncryptionService(secretKeyClientLockerString);
    }

    @Bean(name = "reservationEncryptionService")
    public EncryptionService reservationEncryptionService() throws Exception {
        return new EncryptionService(secretKeyReservationString);
    }
}
