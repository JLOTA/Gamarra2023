package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCategoria")
    private int idCategoria;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;
    @Column(nullable = false)
    private String descripcion;
    
    @OneToMany(mappedBy = "categoria")
    private List<Servicio> servicios;
}
