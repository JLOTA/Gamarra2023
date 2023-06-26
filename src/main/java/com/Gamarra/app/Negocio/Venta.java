package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVenta")
    private int idVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPedido")
    @JsonManagedReference
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    @JsonManagedReference
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipo")
    @JsonManagedReference
    private TipoVenta tipoVenta;

    @Column(nullable = false, unique = true)
    private String correlativo;
    @Column(nullable = false)
    private double descuento;
    @Column(nullable = false)
    private double total;
    @Column(nullable = false)
    private LocalDate fecha;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"idVenta\":").append(idVenta);
        sb.append(", \"correlativo\":\"").append(correlativo).append("\"");
        sb.append(", \"descuento\":").append(descuento);
        sb.append(", \"total\":").append(total);
        sb.append(", \"fecha\":\"").append(fecha).append("\"");
        sb.append(", \"pedido\":").append(pedido != null ? pedido.toString() : "null");
        sb.append(", \"usuario\":").append(usuario != null ? usuario.toString() : "null");
        sb.append(", \"tipoVenta\":").append(tipoVenta != null ? tipoVenta.toString() : "null");
        sb.append("}");
        return sb.toString();
    }

}
