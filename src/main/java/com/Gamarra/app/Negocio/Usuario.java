package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private int idUsuario;

    @OneToOne(optional = false)
    @JoinColumn(name = "IdPerfil")
    private Perfil perfil;

    @OneToOne(optional = true)
    @JoinColumn(name = "IdEmpleado")
    private Empleado empleado;
    
    @Column(nullable = false, unique = true)
    private String usuario;
    @Column(nullable = false)
    private String clave;
    
    @OneToMany(mappedBy = "usuario")
    private List<Servicio> servicios;
    
    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos;
}
