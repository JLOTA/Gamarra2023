package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPedido")
    private int idPedido;

    @ManyToOne
    @JoinColumn(name = "IdCliente")
    @JsonManagedReference
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IdEmpleado")
    @JsonManagedReference
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "IdEstado")
    @JsonManagedReference
    private Estado estado;

    @Column(nullable = false, unique = true)
    private String correlativo;
    @Column(nullable = false)
    private double subtotal;
    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    @JsonManagedReference
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido")
    @JsonBackReference
    private List<DetallePedido> detalles;

    @OneToOne(mappedBy = "pedido")
    @JsonBackReference
    private Venta venta;

    @Override
    public String toString() {
        return "{"
                + "\"idPedido\":" + idPedido
                + ", \"cliente\":" + (cliente != null ? cliente.toString() : "null")
                + ", \"empleado\":" + (empleado != null ? empleado.toString() : "null")
                + ", \"estado\":" + (estado != null ? estado.toString() : "null")
                + ", \"correlativo\":\"" + correlativo + "\""
                + ", \"subtotal\":" + subtotal
                + ", \"fecha\":\"" + fecha + "\""
                + ", \"usuario\":" + (usuario != null ? usuario.toString() : "null")
                + "}";
    }

}
