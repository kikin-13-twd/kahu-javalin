package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REPORTE_CLINICO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Reporte")
    private Integer idReporte;

    @Column(name = "Diagnostico", nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "Tratamiento", nullable = false, columnDefinition = "TEXT")
    private String tratamiento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Cita", nullable = false, unique = true)
    private Cita cita;
}
