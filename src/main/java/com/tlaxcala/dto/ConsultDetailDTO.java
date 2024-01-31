package com.tlaxcala.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

//import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsultDetailDTO {

    @EqualsAndHashCode.Include
    private Integer idDetail;

    @JsonBackReference
    private ConsultDTO consult;

    @NotNull
    private String diagnosis;

    @NotNull
    private String treatment;
    
}
