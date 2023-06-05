package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter @ToString

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPerfil")
    private int idPerfil;
    @Column(nullable = false, unique = true)
    private String nombre;

}
