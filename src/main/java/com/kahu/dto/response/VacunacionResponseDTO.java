package com.kahu.dto.response;

import com.kahu.entity.Vacunacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacunacionResponseDTO {
    private Integer idVacuna;
    private LocalDate fechaProxima;
    private Integer idCatalogoVacuna;
    private String nombreVacuna;
    private Integer idCita;

    public static VacunacionResponseDTO from(Vacunacion v) {
        return new VacunacionResponseDTO(
                v.getIdVacuna(),
                v.getFechaProxima(),
                v.getCatalogoVacuna() != null ? v.getCatalogoVacuna().getIdCatalogoVacuna() : null,
                v.getCatalogoVacuna() != null ? v.getCatalogoVacuna().getNombreVacuna() : null,
                v.getCita() != null ? v.getCita().getIdCita() : null
        );
    }
}
