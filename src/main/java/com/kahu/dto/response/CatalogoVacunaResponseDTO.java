package com.kahu.dto.response;

import com.kahu.entity.CatalogoVacuna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoVacunaResponseDTO {
    private Integer idCatalogoVacuna;
    private String nombreVacuna;

    public static CatalogoVacunaResponseDTO from(CatalogoVacuna cv) {
        return new CatalogoVacunaResponseDTO(cv.getIdCatalogoVacuna(), cv.getNombreVacuna());
    }
}
