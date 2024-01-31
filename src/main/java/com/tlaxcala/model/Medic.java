package com.tlaxcala.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idMedic;

    @Column(nullable = false, length = 70)
    private String firstName;

    @Column(nullable = false, length = 70)
    private String lastName;

    @Column(nullable = false, length = 12)
    private String codMed;

    private String photoUrl;
}
