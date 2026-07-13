package com.kahu.dto.response;

import com.kahu.entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponseDTO {
    private Integer idAnimal;
    private String nombre;
    private Integer edadAnios;
    private String sexo;
    private String tamano;
    private Boolean esterilizado;
    private Integer idRaza;
    private String nombreRaza;
    private Integer idDuenio;
    private String nombreDuenio;

    public static AnimalResponseDTO from(Animal a) {
        return new AnimalResponseDTO(
                a.getIdAnimal(),
                a.getNombre(),
                a.getEdadAnios(),
                a.getSexo(),
                a.getTamano(),
                a.getEsterilizado(),
                a.getRaza() != null ? a.getRaza().getIdRaza() : null,
                a.getRaza() != null ? a.getRaza().getNombreRaza() : null,
                a.getDuenio() != null ? a.getDuenio().getIdUsuario() : null,
                a.getDuenio() != null ? a.getDuenio().getNombre() + " " + a.getDuenio().getApellido() : null
        );
    }
}
