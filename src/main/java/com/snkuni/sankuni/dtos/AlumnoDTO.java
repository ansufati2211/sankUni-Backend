package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AlumnoDTO {
    private UUID idAlumno;
    private UsuarioDTO usuario;
    private String nombreCarrera;
    private String estado;
    private LocalDate fechaIngreso;
}