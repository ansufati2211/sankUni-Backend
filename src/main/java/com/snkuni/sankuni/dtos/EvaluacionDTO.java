package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
public class EvaluacionDTO {
    private Long idEvaluacion;
    
    @NotNull(message = "La sección es obligatoria")
    private Long seccionId;
    
    @NotBlank(message = "El nombre del examen es obligatorio")
    private String nombreExamen;
    
    @Min(1) @Max(100)
    private Integer pesoPorcentaje;
    
    @NotNull(message = "La fecha del examen es obligatoria")
    private LocalDate fechaExamen;
    
    private LocalDateTime fechaPublicacionNotas;
}