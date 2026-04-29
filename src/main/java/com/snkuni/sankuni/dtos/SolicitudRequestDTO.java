package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SolicitudRequestDTO {
    @NotNull(message = "El ID del emisor es obligatorio")
    private Long emisorId;
    
    @NotBlank(message = "El tipo de solicitud es obligatorio")
    private String tipo; // REPROGRAMACION, VERIFICACION_NOTA, OTROS
    
    private Long seccionId; // Opcional
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}