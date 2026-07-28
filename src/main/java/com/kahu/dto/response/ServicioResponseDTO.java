package com.kahu.dto.response;

import com.kahu.entity.Servicio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponseDTO {
    private Integer idServicio;
    private String nombreServicio;
    private BigDecimal precio;
    private Integer tiempoServicioMinutos;
    private Integer idTipoConsulta;
    private String nombreTipoConsulta;

    public static ServicioResponseDTO from(Servicio s) {
        return new ServicioResponseDTO(
                s.getIdServicio(),
                s.getNombreServicio(),
                s.getPrecio(),
                s.getTiempoServicioMinutos(),
                s.getTipoConsulta() != null ? s.getTipoConsulta().getIdTipoConsulta() : null,
                s.getTipoConsulta() != null ? s.getTipoConsulta().getNombreTipo() : null
        );
    }
}
