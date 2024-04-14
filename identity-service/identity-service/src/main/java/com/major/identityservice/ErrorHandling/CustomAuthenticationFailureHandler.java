package com.major.identityservice.ErrorHandling;

import com.major.identityservice.ErrorHandling.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse;
        if (exception instanceof BadCredentialsException) {
            errorResponse = new ErrorResponse(LocalDateTime.now(), "Invalid username or password", "Identity Service");
        } else {
            errorResponse = new ErrorResponse(LocalDateTime.now(), "Authentication failed", "Identity Service");
        }

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
