package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "unidadmedida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUnidad")
    private int idUnidad;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;

    @OneToMany(mappedBy = "unidadMedida")
    private List<Servicio> servicios;
}
