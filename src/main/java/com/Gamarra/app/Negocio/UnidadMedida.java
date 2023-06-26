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
@Table(name = "unidadmedida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUnidad")
    private int idUnidad;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String abreviatura;

    @OneToMany(mappedBy = "unidadMedida")
    @JsonBackReference
    private List<Servicio> servicios;

    @Override
    public String toString() {
        return "{"
                + "\"idUnidad\":" + idUnidad
                + ", \"nombre\":\"" + nombre + "\""
                + ", \"abreviatura\":\"" + abreviatura + "\""
                + "}";
    }

}
