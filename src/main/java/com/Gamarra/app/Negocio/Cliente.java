package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCliente")
    private int idCliente;

    @ManyToOne
    @JoinColumn(name = "IdClase")
    @JsonManagedReference
    private ClaseCliente claseCliente;

    @Column(nullable = false, unique = true)
    private String documento;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    @JsonBackReference
    private List<Pedido> pedidos;

    @Override
    public String toString() {
        return "{"
                + "\"idCliente\":" + idCliente
                + ", \"claseCliente\":" + (claseCliente != null ? claseCliente.toString() : "null")
                + ", \"documento\":\"" + documento + "\""
                + ", \"nombre\":\"" + nombre + "\""
                + ", \"telefono\":\"" + telefono + "\""
                + ", \"direccion\":\"" + direccion + "\""
                + "}";
    }

}
