package com.kahu.dto.response;

import com.kahu.entity.TipoConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoConsultaResponseDTO {
    private Integer idTipoConsulta;
    private String nombreTipo;
    private Integer idRolRequerido;
    private String nombreRolRequerido;

    public static TipoConsultaResponseDTO from(TipoConsulta t) {
        return new TipoConsultaResponseDTO(
                t.getIdTipoConsulta(),
                t.getNombreTipo(),
                t.getRolRequerido() != null ? t.getRolRequerido().getIdRol() : null,
                t.getRolRequerido() != null ? t.getRolRequerido().getNombreRol() : null
        );
    }
}
