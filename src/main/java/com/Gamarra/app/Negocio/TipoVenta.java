package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "tipoventa")
public class TipoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipo")
    private int idTipo;
    @Column(nullable = false, unique = true)
    private String nombre;

}
