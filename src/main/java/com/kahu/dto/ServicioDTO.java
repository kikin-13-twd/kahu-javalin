package com.kahu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String nombreServicio;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "El tiempo del servicio es obligatorio")
    @Min(value = 1, message = "El tiempo debe ser mayor a 0 minutos")
    private Integer tiempoServicioMinutos;

    @NotNull(message = "El tipo de consulta es obligatorio")
    private Integer idTipoConsulta;
}
