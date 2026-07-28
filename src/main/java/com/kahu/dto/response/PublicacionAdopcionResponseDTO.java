package com.kahu.dto.response;

import com.kahu.entity.PublicacionAdopcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionAdopcionResponseDTO {
    private Integer idPublicacion;
    private String descripcion;
    private String urlImagen;
    private String estado;
    private Integer idAnimal;
    private String nombreAnimal;

    public static PublicacionAdopcionResponseDTO from(PublicacionAdopcion p) {
        return new PublicacionAdopcionResponseDTO(
                p.getIdPublicacion(),
                p.getDescripcion(),
                p.getUrlImagen(),
                p.getEstado(),
                p.getAnimal() != null ? p.getAnimal().getIdAnimal() : null,
                p.getAnimal() != null ? p.getAnimal().getNombre() : null
        );
    }
}
