package com.kahu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ESPECIE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Especie")
    private Integer idEspecie;

    @Column(name = "Nombre_Especie", unique = true, nullable = false, length = 50)
    private String nombreEspecie;
}
