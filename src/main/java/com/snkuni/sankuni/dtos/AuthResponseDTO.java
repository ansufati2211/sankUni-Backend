package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String token;
    private String nombre;
    private String rol;
    private Long usuarioId; // Muy útil para que el frontend pida el perfil luego
}