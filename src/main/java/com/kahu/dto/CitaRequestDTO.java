package com.kahu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    private String sintomasMotivo;

    @NotNull(message = "El animal es obligatorio")
    private Integer idAnimal;

    private Integer idPersonal;

    @NotNull(message = "El servicio es obligatorio")
    private Integer idServicio;
}
