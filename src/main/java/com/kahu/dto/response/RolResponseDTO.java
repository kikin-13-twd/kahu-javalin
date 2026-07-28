package com.kahu.dto.response;

import com.kahu.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolResponseDTO {
    private Integer idRol;
    private String nombreRol;

    public static RolResponseDTO from(Rol rol) {
        return new RolResponseDTO(rol.getIdRol(), rol.getNombreRol());
    }
}
