package com.tlaxcala.dto;

// se encuentran disponibles desde java 15 en adelante
// sirven para crear objetos inmutables: no puedes trabajar con setters, no puedo modificar sus atributos
public record PatientRecord(
    Integer idPatient,
    String primaryName,
    String surname,
    String dni,
    String address,
    String phone,
    String email
) {
    
}
