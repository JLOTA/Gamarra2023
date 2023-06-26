package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tipoventa")
public class TipoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipo")
    private int idTipo;
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "tipoVenta")
    @JsonBackReference
    private List<Venta> ventas;

    @Override
    public String toString() {
        return "{"
                + "\"idTipo\":" + idTipo
                + ", \"nombre\":\"" + nombre + "\""
                + "}";
    }

}
