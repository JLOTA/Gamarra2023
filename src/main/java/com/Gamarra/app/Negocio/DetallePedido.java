package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "detallepedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalle")
    private int idDetalle; 
    
    @ManyToOne
    @JoinColumn(name = "IdPedido")
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "IdServicio")
    private Servicio servicio;
    
    @Column(nullable = false)
    private String observacion;
    @Column(nullable = false)
    private double cantidad;
    @Column(nullable = false)
    private double totalUnitario;

}
