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
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdServicio")
    private int idServicio;

    @ManyToOne
    @JoinColumn(name = "IdCategoria")
    @JsonManagedReference
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "IdSubcategoria")
    @JsonManagedReference
    private Subcategoria subcategoria;

    @ManyToOne
    @JoinColumn(name = "IdUnidad")
    @JsonManagedReference
    private UnidadMedida unidadMedida;

    @Column(nullable = false, unique = true)
    private String abreviatura;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private double precio;

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    @JsonManagedReference
    private Usuario usuario;

    @OneToMany(mappedBy = "servicio")
    @JsonBackReference
    private List<DetallePedido> detallesPedido;

    @Override
    public String toString() {
        return "{"
                + "\"idServicio\":" + idServicio
                + ", \"categoria\":" + (categoria != null ? categoria.toString() : "null")
                + ", \"subcategoria\":" + (subcategoria != null ? subcategoria.toString() : "null")
                + ", \"unidadMedida\":" + (unidadMedida != null ? unidadMedida.toString() : "null")
                + ", \"abreviatura\":\"" + abreviatura + "\""
                + ", \"descripcion\":\"" + descripcion + "\""
                + ", \"precio\":" + precio
                + ", \"usuario\":" + (usuario != null ? usuario.toString() : "null")
                + "}";
    }

}
