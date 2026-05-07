package com.snkuni.sankuni.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor // <--- ESTA ES VITAL
@AllArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private String dni;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private String email;
    private String rol;
}