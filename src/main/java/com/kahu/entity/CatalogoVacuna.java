package com.kahu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CATALOGO_VACUNA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoVacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Catalogo_Vacuna")
    private Integer idCatalogoVacuna;

    @Column(name = "Nombre_Vacuna", unique = true, nullable = false, length = 100)
    private String nombreVacuna;
}
