package com.snkuni.sankuni.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaDTO {
    private Long alumnoId;
    private Boolean presente;

    // Campos enriquecidos para la vista del alumno
    private LocalDate fecha;
    private String nombreCurso;
    private Long seccionId;
    private String cicloAcademico;
}