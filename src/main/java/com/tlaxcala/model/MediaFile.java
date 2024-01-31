package com.tlaxcala.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MediaFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFile;

    @Column(length = 50, nullable = false)
    private String filename;

    @Column(length = 20, nullable = false)
    private String fileType;

    @Column(name = "content", nullable = false)
    private byte[] value;
}
