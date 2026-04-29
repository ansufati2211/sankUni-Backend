package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class CursoDTO {
    private UUID idCurso;
    
    @NotNull(message = "El ID de la carrera es obligatorio")
    private UUID carreraId;
    
    private String nombreCarrera;
    
    @NotBlank(message = "El nombre del curso es obligatorio")
    private String nombre;
    
    @Min(value = 1, message = "Los créditos deben ser mayor a 0")
    private Integer creditos;
    
    private String temarioUrl;
    private String descripcionInformativa;
}