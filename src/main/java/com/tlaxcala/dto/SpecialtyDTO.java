package com.tlaxcala.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialtyDTO {

    @EqualsAndHashCode.Include
    private Integer idSpecialty;

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String description;
    
}
