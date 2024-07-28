package com.dac.dac.auth;

import com.dac.dac.payload.response.AuthenticationResponse;
import com.dac.dac.payload.request.LoginRequestDto;
import com.dac.dac.payload.request.RegisterRequestDto;
import com.dac.dac.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;



    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(authenticationService.register(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }
    @GetMapping("/account-verify")
    ResponseEntity verifyAccount(@RequestParam("token") String token){
        return new ResponseEntity<>(authenticationService.accountActivation(token), HttpStatus.OK);
    }
}
