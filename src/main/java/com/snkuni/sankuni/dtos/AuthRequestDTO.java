package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}