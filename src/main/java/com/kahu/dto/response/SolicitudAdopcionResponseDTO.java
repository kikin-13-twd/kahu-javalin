package com.kahu.dto.response;

import com.kahu.entity.SolicitudAdopcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcionResponseDTO {
    private Integer idSolicitud;
    private LocalDate fechaSolicitud;
    private String estado;
    private Integer idUsuarioInteresado;
    private String nombreUsuario;
    private Integer idPublicacion;

    public static SolicitudAdopcionResponseDTO from(SolicitudAdopcion s) {
        return new SolicitudAdopcionResponseDTO(
                s.getIdSolicitud(),
                s.getFechaSolicitud(),
                s.getEstado(),
                s.getUsuarioInteresado() != null ? s.getUsuarioInteresado().getIdUsuario() : null,
                s.getUsuarioInteresado() != null
                        ? s.getUsuarioInteresado().getNombre() + " " + s.getUsuarioInteresado().getApellido()
                        : null,
                s.getPublicacion() != null ? s.getPublicacion().getIdPublicacion() : null
        );
    }
}
