package com.kahu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Email invalido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    private String direccion;

    @NotNull(message = "El rol es obligatorio")
    private Integer idRol;
}
