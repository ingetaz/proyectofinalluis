package com.tlaxcala.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable { // que es lo que solicito para autenticarme en el login
    
    private String username;
    private String password;
}
