package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEmpleado")
    private int idEmpleado;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String correo;
    
    
}
