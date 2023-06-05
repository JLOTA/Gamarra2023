package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPedido")
    private int idPedido;

    @ManyToOne
    @JoinColumn(name = "IdCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IdEmpleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "IdEstado")
    private Estado estado;

    @Column(nullable = false, unique = true)
    private String correlativo;
    @Column(nullable = false)
    private double subtotal;
    @Column(nullable = false)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detalles;
}
