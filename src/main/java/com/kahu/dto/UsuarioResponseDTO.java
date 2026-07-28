package com.kahu.dto;

import com.kahu.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
    private Integer idRol;
    private String nombreRol;

    public static UsuarioResponseDTO from(Usuario u) {
        return new UsuarioResponseDTO(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTelefono(),
                u.getDireccion(),
                u.getFechaRegistro(),
                u.getRol() != null ? u.getRol().getIdRol() : null,
                u.getRol() != null ? u.getRol().getNombreRol() : null
        );
    }
}
