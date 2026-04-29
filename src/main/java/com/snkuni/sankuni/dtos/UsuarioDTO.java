package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UsuarioDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String rol;
}