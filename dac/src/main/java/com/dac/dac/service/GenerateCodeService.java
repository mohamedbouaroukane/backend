package com.dac.dac.service;

import com.dac.dac.repository.ParcelRepository;
import com.dac.dac.utils.CourierParcelLockerCheckerModel;
import com.dac.dac.utils.EmailConfirmationToken;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class GenerateCodeService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";
    protected long generateRandomInteger(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        sb.append(random.nextInt(9) + 1);

        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return Long.parseLong(sb.toString());
    }
    protected String generateRandomString(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }


}
