package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPerfil")
    private int idPerfil;
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "perfil")
    @JsonBackReference
    private List<Usuario> usuarios;

    @Override
    public String toString() {
        return "{" + "\"idPerfil\":" + idPerfil + ", \"nombre\":\"" + nombre + "\"}";
    }

}
