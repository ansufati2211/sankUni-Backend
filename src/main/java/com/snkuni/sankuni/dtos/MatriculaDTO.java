package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class MatriculaDTO {
    private Long idMatricula;
    private String nombreAlumno;
    private String cursoYSeccion;
    private BigDecimal notaFinal;
    private LocalDateTime fechaMatricula;
}