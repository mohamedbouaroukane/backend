package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.entity.User;
import com.dac.dac.exption.TokenException;
import com.dac.dac.repository.UserRepository;
import com.dac.dac.utils.EmailConfirmationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailConfirmationService {
    Logger logger = LoggerFactory.getLogger(EmailConfirmationService.class);


    private EmailConfirmationToken emailConfirmationToken;

    @Autowired
    private GenerateEmailConfirmationCode generateEmailConfirmationCode;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("tokenEncryptionService")
    private EncryptionService encryptionService;


    public String createToken(int userId) {

       EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
                .token(generateEmailConfirmationCode.generateConfirmationToken())
               .creationTime(LocalDateTime.now())
               .expiryTime(LocalDateTime.now().plusMinutes(15))
               .userId(userId)
               .build();

       return generateToken(emailConfirmationToken);

    }

    @Transactional
    public boolean validateToken(String token) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String decryptedCode = encryptionService.decrypt(token);

            EmailConfirmationToken emailConfirmationToken = mapper.readValue(decryptedCode, EmailConfirmationToken.class);
            logger.info(String.valueOf(emailConfirmationToken.getUserId()));
            User user = userRepository.findById(emailConfirmationToken.getUserId()).orElseThrow(()-> new TokenException(ExceptionMessages.TOKEN_NOT_VALID_EXCEPTION));

            if(user.getEnabled()){
                throw new TokenException(ExceptionMessages.TOKEN_ALREADY_VERIFIED_EXCEPTION);
            }
            logger.info(String.valueOf(emailConfirmationToken.getExpiryTime()));
            if(isExpired(emailConfirmationToken.getExpiryTime())){
                if(!user.getEnabled()){
                    //TODO: resend a new token
                }
                throw new TokenException(ExceptionMessages.TOKEN_EXPIRED_EXCEPTION);
            }

            user.setEnabled(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TokenException(ExceptionMessages.TOKEN_NOT_VALID_EXCEPTION);
        }

    }

    private String generateToken(EmailConfirmationToken emailConfirmationToken){
        ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String token ="";

        try {
            String code = mapper.writeValueAsString(emailConfirmationToken);
            token = encryptionService.encrypt(code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    private boolean isExpired(LocalDateTime expiryTime){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expiryTime);
    }

}
