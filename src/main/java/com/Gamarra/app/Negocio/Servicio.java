package com.Gamarra.app.Negocio;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @Getter @Setter

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdServicio")
    private int idServicio;

    @ManyToOne
    @JoinColumn(name = "IdCategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "IdSubcategoria")
    private Subcategoria subcategoria;

    @ManyToOne
    @JoinColumn(name = "IdUnidad")
    private UnidadMedida unidadMedida;
    
    @Column(nullable = false, unique = true)
    private String abreviatura;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private double precio;
    
    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;

    @Override
    public String toString() {
        return "Servicio{" + "idServicio=" + idServicio + ", categoria=" + categoria.getNombre() + ", subcategoria=" + subcategoria.getNombre() + ", unidadMedida=" + unidadMedida.getNombre() + ", abreviatura=" + abreviatura + ", descripcion=" + descripcion + ", precio=" + precio + ", usuario=" + usuario.getUsuario() + '}';
    }

    
}
