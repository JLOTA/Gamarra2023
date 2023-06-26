package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEstado")
    private int idEstado;
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "estado")
    @JsonBackReference
    private List<Pedido> pedidos;

    @Override
    public String toString() {
        return "{"
                + "\"idEstado\":" + idEstado
                + ", \"nombre\":\"" + nombre + "\""
                + "}";
    }

}
