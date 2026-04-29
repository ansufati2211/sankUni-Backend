package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class DocenteDTO {
    private UUID idDocente;
    private UsuarioDTO usuario;
    private String especialidad;
    private String estado;
}