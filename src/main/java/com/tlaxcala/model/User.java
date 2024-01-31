package com.tlaxcala.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_data")
public class User {

    @Id
    @EqualsAndHashCode.Include
    private Integer idUser;

    @Column(nullable = false, length = 60, unique = true)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    // forma 2: many to many
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", 
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "idRole")
    )
    private List<Role> roles;
}
