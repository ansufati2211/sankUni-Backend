package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class UsuarioDTO {
    private UUID idUsuario;
    private String nombreCompleto;
    private String email;
    private String rol;
}