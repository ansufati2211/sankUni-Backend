package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRequestDTO {
    @NotNull(message = "El ID del emisor es obligatorio")
    private Long emisorId;
    
    @NotBlank(message = "El tipo de solicitud es obligatorio")
    private String tipo; 
    
    private Long seccionId; // Puede ser null
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}