package com.tlaxcala.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tlaxcala.model.Medic;
import com.tlaxcala.model.Patient;
import com.tlaxcala.model.Specialty;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsultDTO {

    @EqualsAndHashCode.Include
    private Integer idConsult;

    @NotNull
    private Patient patient;

    @NotNull
    private Medic medic;

    @NotNull
    private Specialty specialty;

    @NotNull
    private String numConsult;

    @NotNull
    private LocalDateTime consultDate;

    @JsonManagedReference
    @NotNull
    private List<ConsultDetailDTO> details;
    
}
