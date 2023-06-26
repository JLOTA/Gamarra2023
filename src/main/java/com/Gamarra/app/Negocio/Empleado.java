package com.Gamarra.app.Negocio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEmpleado")
    private int idEmpleado;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String correo;

    @OneToOne(mappedBy = "empleado")
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "empleado")
    @JsonBackReference
    private List<Pedido> pedidos;

    @Override
    public String toString() {
        return "{"
                + "\"idEmpleado\":" + idEmpleado
                + ", \"dni\":\"" + dni + "\""
                + ", \"nombre\":\"" + nombre + "\""
                + ", \"apellido\":\"" + apellido + "\""
                + ", \"telefono\":\"" + telefono + "\""
                + ", \"correo\":\"" + correo + "\""
                + "}";
    }

}
