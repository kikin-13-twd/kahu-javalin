package com.kahu.dto.response;

import com.kahu.entity.Raza;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazaResponseDTO {
    private Integer idRaza;
    private String nombreRaza;
    private Integer idEspecie;
    private String nombreEspecie;

    public static RazaResponseDTO from(Raza r) {
        return new RazaResponseDTO(
                r.getIdRaza(),
                r.getNombreRaza(),
                r.getEspecie() != null ? r.getEspecie().getIdEspecie() : null,
                r.getEspecie() != null ? r.getEspecie().getNombreEspecie() : null
        );
    }
}
