package com.kahu.dto.response;

import com.kahu.entity.ReporteClinico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteClinicoResponseDTO {
    private Integer idReporte;
    private String diagnostico;
    private String tratamiento;
    private Integer idCita;

    public static ReporteClinicoResponseDTO from(ReporteClinico r) {
        return new ReporteClinicoResponseDTO(
                r.getIdReporte(),
                r.getDiagnostico(),
                r.getTratamiento(),
                r.getCita() != null ? r.getCita().getIdCita() : null
        );
    }
}
