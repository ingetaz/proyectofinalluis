package com.tlaxcala.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// CLASE POJO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {

    private LocalDateTime datetime;
    private String message;
    private String details;
    
}
