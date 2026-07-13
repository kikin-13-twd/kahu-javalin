package com.kahu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edadAnios;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    @NotBlank(message = "El tamano es obligatorio")
    private String tamano;

    @NotNull(message = "Debe indicar si esta esterilizado")
    private Boolean esterilizado;

    @NotNull(message = "La raza es obligatoria")
    private Integer idRaza;

    private Integer idDuenio;
}
