package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "clasecliente")
public class ClaseCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdClase")
    private int idClase;
    @Column(nullable = false)
    private String nombre;

}
