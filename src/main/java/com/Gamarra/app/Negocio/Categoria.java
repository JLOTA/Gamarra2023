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
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCategoria")
    private int idCategoria;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;
    @Column(nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "categoria")
    @JsonBackReference
    private List<Servicio> servicios;

    @Override
    public String toString() {
        return "{"
                + "\"idCategoria\":\"" + idCategoria + "\""
                + ", \"nombre\":\"" + nombre + "\""
                + ", \"abreviatura\":\"" + abreviatura + "\""
                + ", \"descripcion\":\"" + descripcion + "\""
                + "}";
    }

}
