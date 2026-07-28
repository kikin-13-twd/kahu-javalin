package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TIPO_CONSULTA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Tipo_Consulta")
    private Integer idTipoConsulta;

    @Column(name = "Nombre_Tipo", nullable = false, length = 100)
    private String nombreTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Rol_Requerido", nullable = false)
    private Rol rolRequerido;
}
