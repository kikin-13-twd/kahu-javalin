package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "SERVICIO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Servicio")
    private Integer idServicio;

    @Column(name = "Nombre_Servicio", nullable = false, length = 150)
    private String nombreServicio;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Tiempo_Servicio_Minutos", nullable = false)
    private Integer tiempoServicioMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Tipo_Consulta", nullable = false)
    private TipoConsulta tipoConsulta;
}
