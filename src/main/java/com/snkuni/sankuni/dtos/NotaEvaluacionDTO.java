package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class NotaEvaluacionDTO {
    private Long idNota;
    
    @NotNull(message = "La evaluación es obligatoria")
    private Long evaluacionId;
    
    @NotNull(message = "El alumno es obligatorio")
    private Long alumnoId;
    
    private String nombreAlumno;
    
    @DecimalMin(value = "0.00") @DecimalMax(value = "20.00")
    private BigDecimal nota;
    
    private LocalDateTime fechaRegistro;
}