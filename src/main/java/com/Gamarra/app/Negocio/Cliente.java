package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCliente")
    private int idCliente;

    @ManyToOne
    @JoinColumn(name = "IdClase")
    private ClaseCliente claseCliente;
    @Column(nullable = false, unique = true)
    private String documento;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String direccion;

}
