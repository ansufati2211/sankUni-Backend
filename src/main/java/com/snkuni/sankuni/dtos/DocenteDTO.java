package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DocenteDTO {
    private Long idDocente;
    private UsuarioDTO usuario;
    private String especialidad;
    private String estado;
}