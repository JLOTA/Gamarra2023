package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "subcategoria")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSubcategoria")
    private int idSubcategoria;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;
    
    @OneToMany(mappedBy = "subcategoria")
    private List<Servicio> servicios;

}
