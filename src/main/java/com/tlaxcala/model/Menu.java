package com.tlaxcala.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Menu {
    
    @Id
    @EqualsAndHashCode.Include
    private Integer idMenu;

    @Column(nullable = false, length = 20)
    private String icon;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_role",
        joinColumns = @JoinColumn(name = "id_menu", referencedColumnName = "idMenu"),
        inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "idRole")    
    )
    private List<Role> roles;

}
