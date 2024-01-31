package com.tlaxcala.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlaxcala.exception.CustomErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // para manejar mensajes de error/excepciones

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String exceptionMsg = (String) request.getAttribute("exception"); // capturar las excepciones de validaciones de tokens
        if (exceptionMsg == null) {
            exceptionMsg = "Token not found or invalid";
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), exceptionMsg, request.getRequestURI());
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 prohibido/no-authorizado
        response.getWriter().write(convertObjectToJson(errorResponse));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    } 

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.writeValueAsString(object); // lo transforma a formato json
    }
    
}
