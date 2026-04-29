package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotaEvaluacionDTO {
    private UUID idNota;
    
    @NotNull(message = "La evaluación es obligatoria")
    private UUID evaluacionId;
    
    @NotNull(message = "El alumno es obligatorio")
    private UUID alumnoId;
    
    private String nombreAlumno;
    
    @DecimalMin(value = "0.00") @DecimalMax(value = "20.00")
    private BigDecimal nota;
    
    private LocalDateTime fechaRegistro;
}