package com.dac.dac.secuity;

import com.dac.dac.config.SecurityConfig;
import com.dac.dac.entity.User;
import com.dac.dac.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
                String token = jwtTokenHeader.substring("Bearer ".length());
                String userEmail = jwtTokenService.extractUserEmail(token);
                if (userEmail != null) {
                    User user = (User) userService.loadUserByUsername(userEmail);
                    if (jwtTokenService.isTokenValid(token, user)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request,response);
    }
}
