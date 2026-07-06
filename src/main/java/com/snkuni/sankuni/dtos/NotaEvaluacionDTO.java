package com.snkuni.sankuni.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaEvaluacionDTO {
    private Long idNota;
    private Long evaluacionId;
    private Long alumnoId;
    private String nombreAlumno;
    private BigDecimal nota;
    private String comentario;
    private LocalDateTime fechaRegistro;

    // Campos enriquecidos para la vista del alumno
    private String nombreExamen;
    private Integer pesoPorcentaje;
    private LocalDate fechaExamen;
    private String nombreCurso;
    private Long seccionId;
    private String cicloAcademico;
}