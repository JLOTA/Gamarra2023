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
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private int idUsuario;

    @ManyToOne
    @JoinColumn(name = "IdPerfil")
    @JsonManagedReference
    private Perfil perfil;

    @OneToOne(optional = true)
    @JoinColumn(name = "IdEmpleado")
    @JsonManagedReference
    private Empleado empleado;

    @Column(nullable = false, unique = true)
    private String usuario;
    @Column(nullable = false)
    private String clave;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference
    private List<Servicio> servicios;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference
    private List<Venta> ventas;

    @Override
    public String toString() {
        return "{"
                + "\"idUsuario\":" + idUsuario
                + ", \"perfil\":" + (perfil != null ? perfil.toString() : "null")
                + ", \"empleado\":" + (empleado != null ? empleado.toString() : "null")
                + ", \"usuario\":\"" + usuario + "\""
                + ", \"clave\":\"" + clave + "\""
                + "}";
    }

}
