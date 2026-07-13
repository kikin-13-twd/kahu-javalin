package com.kahu.dto.response;

import com.kahu.entity.Cita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponseDTO {
    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String sintomasMotivo;
    private String estado;
    private Integer idAnimal;
    private String nombreAnimal;
    private Integer idPersonal;
    private String nombrePersonal;
    private Integer idServicio;
    private String nombreServicio;

    public static CitaResponseDTO from(Cita c) {
        return new CitaResponseDTO(
                c.getIdCita(),
                c.getFecha(),
                c.getHora(),
                c.getSintomasMotivo(),
                c.getEstado(),
                c.getAnimal() != null ? c.getAnimal().getIdAnimal() : null,
                c.getAnimal() != null ? c.getAnimal().getNombre() : null,
                c.getPersonal() != null ? c.getPersonal().getIdUsuario() : null,
                c.getPersonal() != null ? c.getPersonal().getNombre() + " " + c.getPersonal().getApellido() : null,
                c.getServicio() != null ? c.getServicio().getIdServicio() : null,
                c.getServicio() != null ? c.getServicio().getNombreServicio() : null
        );
    }
}
