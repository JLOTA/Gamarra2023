package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "detallepedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalle")
    private int idDetalle;

    @ManyToOne
    @JoinColumn(name = "IdPedido")
    @JsonManagedReference
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "IdServicio")
    @JsonManagedReference
    private Servicio servicio;

    @Column(nullable = false)
    private String observacion;
    @Column(nullable = false)
    private double cantidad;
    @Column(nullable = false)
    private double totalUnitario;

    @Override
    public String toString() {
        return "{"
                + "\"idDetalle\":" + idDetalle
                + ", \"pedido\":" + (pedido != null ? pedido.toString() : "null")
                + ", \"servicio\":" + (servicio != null ? servicio.toString() : "null")
                + ", \"observacion\":\"" + observacion + "\""
                + ", \"cantidad\":" + cantidad
                + ", \"totalUnitario\":" + totalUnitario
                + "}";
    }

}
