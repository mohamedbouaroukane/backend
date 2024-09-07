package com.dac.dac.service;

import com.dac.dac.constants.UserRole;
import com.dac.dac.constants.UserRoleValue;
import com.dac.dac.entity.User;
import com.dac.dac.exption.DuplicatedEntryException;
import com.dac.dac.exption.LoginFailedException;
import com.dac.dac.exption.RegisterException;
import com.dac.dac.mapper.UserMapper;
import com.dac.dac.payload.response.AuthenticationResponse;
import com.dac.dac.payload.request.LoginRequestDto;
import com.dac.dac.payload.request.RegisterRequestDto;
import com.dac.dac.repository.UserRepository;
import com.dac.dac.secuity.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserMapper  userMapper;

    private final PasswordEncoder passwordEncoder;

    private final  UserRepository userRepository;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final GenerateCodeService generateCodeService;

    private final EmailConfirmationService emailConfirmationService;



    @Transactional
    public Boolean register(RegisterRequestDto registerRequestDto) {
            verifyMailPhone(registerRequestDto.getEmail(),registerRequestDto.getPhone());
            User user ;


            if(UserRoleValue.CLIENT.equalsIgnoreCase(registerRequestDto.getUserRole())){
                user = userMapper.mapToClient(registerRequestDto);
                user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
                userRepository.save(user);
                String token = emailConfirmationService.createToken(user.getId());
                emailService.sendMailConfirmation(user.getEmail(),user.getFullName(),"http://localhost:8080/api/v1/auth/account-verify?token="+token);
            }else if(UserRoleValue.COURIER.equalsIgnoreCase(registerRequestDto.getUserRole())){
                user = userMapper.mapToCourier(registerRequestDto);
                user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
                user.setEnabled(true);
                userRepository.save(user);
            }else{
                throw new RegisterException("Invalid user role");
            }



            return true;
    }

    public AuthenticationResponse login(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword())
            );
            User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()-> new LoginFailedException("Email or Password are not correct"));

            if((loginRequestDto.getUserRole().equals(UserRoleValue.CLIENT) && user.getUserRole().equals(UserRole.CLIENT))
                || (loginRequestDto.getUserRole().equals(UserRoleValue.COURIER) && user.getUserRole().equals(UserRole.COURIER))
                || (loginRequestDto.getUserRole().equals(UserRoleValue.ADMIN) && user.getUserRole().equals(UserRole.ADMIN))
            ){
                String token = jwtTokenService.generateToken(user);

                return AuthenticationResponse.builder().token(token).email(user.getEmail()).fullName(user.getFullName()).userID(user.getId()).build();
            }

            throw new LoginFailedException("Email or password are incorrect");

        }catch (DisabledException e){
            throw new LoginFailedException("Please check your email to confirm it. ");
        }catch (Exception e){
            throw new LoginFailedException("Email or password are incorrect");
        }

    }



    private void verifyMailPhone(String email, String phone){
        boolean emailExist = userRepository.existsByEmail(email);
        boolean phoneExist = userRepository.existsByPhone(phone);
        Map<String, String> fields = new HashMap<>();
        if( emailExist && phoneExist){
            fields.put("email","This email already exists");
            fields.put("phone","This phone already exists");
        }else if(emailExist || phoneExist){
            if(emailExist){
                fields.put("email","This email"+email+" already exists");
            }else{
                fields.put("phone","This phone"+ phone+" already exists");
            }
        }

        if(!fields.isEmpty()){
            throw new DuplicatedEntryException(fields);
        }
    }

    public boolean accountActivation(String token){
        return emailConfirmationService.validateToken(token);
    }
}
