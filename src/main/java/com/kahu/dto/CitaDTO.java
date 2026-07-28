package com.kahu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String sintomasMotivo;
    private String estado;
    private String nombreAnimal;
    private String nombrePersonal;
    private String nombreServicio;
}
