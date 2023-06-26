package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "clasecliente")
public class ClaseCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdClase")
    private int idClase;
    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "claseCliente")
    @JsonBackReference
    private List<Cliente> clientes;

    @Override
    public String toString() {
        return "{"
                + "\"idClase\":\"" + idClase + "\""
                + ", \"nombre\":\"" + nombre + "\""
                + "}";
    }

}
