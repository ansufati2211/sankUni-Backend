package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDTO {
    private Long idUsuario;
    private String dni;
    private String nombres;
    private String apellidos;
    private String nombreCompleto; // Lo mantenemos para compatibilidad con otras vistas
    private String email;
    private String rol;
}