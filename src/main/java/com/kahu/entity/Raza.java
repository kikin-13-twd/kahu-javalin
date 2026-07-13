package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RAZA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Raza")
    private Integer idRaza;

    @Column(name = "Nombre_Raza", nullable = false, length = 100)
    private String nombreRaza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Especie", nullable = false)
    private Especie especie;
}
