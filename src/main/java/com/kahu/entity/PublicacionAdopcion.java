package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PUBLICACION_ADOPCION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Publicacion")
    private Integer idPublicacion;

    @Column(name = "Descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "Url_Imagen", nullable = false, length = 255)
    private String urlImagen;

    @Column(name = "Estado", length = 50)
    private String estado = "Disponible";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Animal", nullable = false, unique = true)
    private Animal animal;
}
