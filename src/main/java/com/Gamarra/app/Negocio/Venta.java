package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVenta")
    private int idVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPedido")
    private Pedido pedido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipo")
    private TipoVenta tipoVenta;
    
    @Column(nullable = false, unique = true)
    private String correlativo;
    @Column(nullable = false)
    private double descuento;
    @Column(nullable = false)
    private double total;
    @Column(nullable = false)
    private LocalDate fecha;

}
