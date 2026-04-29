package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;


@Data
@Builder
public class AlumnoDTO {
    private Long idAlumno;
    private UsuarioDTO usuario;
    private String nombreCarrera;
    private String estado;
    private LocalDate fechaIngreso;
}