package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class SolicitudRequestDTO {
    @NotNull(message = "El ID del emisor es obligatorio")
    private UUID emisorId;
    
    @NotBlank(message = "El tipo de solicitud es obligatorio")
    private String tipo; // REPROGRAMACION, VERIFICACION_NOTA, OTROS
    
    private UUID seccionId; // Opcional
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}