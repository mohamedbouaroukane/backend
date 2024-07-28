package com.dac.dac.config;

import com.dac.dac.constants.UserRole;
import com.dac.dac.secuity.JwtAuthenticationFilter;
import com.dac.dac.service.EmailConfirmationService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    String [] PUBLIC_END_POINTS = {"/api/v1/auth/*"};

    String [] ADMIN_GET_END_POINTS = {"/api/v1/auth/*"};


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors-> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("**").permitAll()
                                .requestMatchers("/api/v1/courier/parcel-locker/","/api/v1/rsc/entry","/api/v1/rsc/exit","api/v1/rsc/").hasAuthority(UserRole.COURIER.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
