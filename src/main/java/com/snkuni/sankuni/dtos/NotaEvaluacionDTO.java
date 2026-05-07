package com.snkuni.sankuni.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private LocalDateTime fechaRegistro;
}