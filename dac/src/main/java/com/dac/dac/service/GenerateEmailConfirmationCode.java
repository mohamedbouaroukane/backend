package com.dac.dac.service;


import org.springframework.stereotype.Service;

@Service
public class GenerateEmailConfirmationCode extends GenerateCodeService{

    private String TOKEN_CODE_PREFIX = "TK";

    public String generateConfirmationToken(){
        return TOKEN_CODE_PREFIX+generateRandomInteger(6) ;
    }

}
