package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "SOLICITUD_ADOPCION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Solicitud")
    private Integer idSolicitud;

    @Column(name = "Fecha_Solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "Estado", length = 50)
    private String estado = "Pendiente";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario_Interesado", nullable = false)
    private Usuario usuarioInteresado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Publicacion", nullable = false)
    private PublicacionAdopcion publicacion;
}
