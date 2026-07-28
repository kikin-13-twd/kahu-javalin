package com.kahu.dto.response;

import com.kahu.entity.Especie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecieResponseDTO {
    private Integer idEspecie;
    private String nombreEspecie;

    public static EspecieResponseDTO from(Especie e) {
        return new EspecieResponseDTO(e.getIdEspecie(), e.getNombreEspecie());
    }
}
