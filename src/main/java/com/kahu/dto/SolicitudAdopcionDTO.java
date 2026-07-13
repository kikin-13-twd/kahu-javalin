package com.kahu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcionDTO {

    @NotNull(message = "El usuario interesado es obligatorio")
    private Integer idUsuarioInteresado;

    @NotNull(message = "La publicacion es obligatoria")
    private Integer idPublicacion;
}
