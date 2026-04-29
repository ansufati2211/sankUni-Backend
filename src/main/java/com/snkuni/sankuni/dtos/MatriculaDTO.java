package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MatriculaDTO {
    private UUID idMatricula;
    private String nombreAlumno;
    private String cursoYSeccion;
    private BigDecimal notaFinal;
    private LocalDateTime fechaMatricula;
}