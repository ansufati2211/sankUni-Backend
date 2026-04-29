package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class CarreraDTO {
    private UUID idCarrera;
    
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    private String nombre;
    
    private String descripcion;
    private Boolean estado;
}