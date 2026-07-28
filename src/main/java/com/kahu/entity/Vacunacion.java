package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "VACUNACION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacunacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Vacuna")
    private Integer idVacuna;

    @Column(name = "Fecha_Proxima")
    private LocalDate fechaProxima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Catalogo_Vacuna", nullable = false)
    private CatalogoVacuna catalogoVacuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Cita", nullable = false)
    private Cita cita;
}
