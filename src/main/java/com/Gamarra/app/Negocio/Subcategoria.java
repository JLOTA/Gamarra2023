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
@Table(name = "subcategoria")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSubcategoria")
    private int idSubcategoria;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;

    @OneToMany(mappedBy = "subcategoria")
    @JsonBackReference
    private List<Servicio> servicios;

    @Override
    public String toString() {
        return "{"
                + "\"idSubcategoria\":" + idSubcategoria
                + ", \"nombre\":\"" + nombre + "\""
                + ", \"abreviatura\":\"" + abreviatura + "\""
                + "}";
    }

}
