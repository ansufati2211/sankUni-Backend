package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class AsistenciaRequestDTO {
    @NotNull(message = "El ID de la sección es obligatorio")
    private UUID seccionId;

    @NotNull(message = "El ID del alumno es obligatorio")
    private UUID alumnoId;

    @NotNull(message = "El estado de asistencia es obligatorio")
    private Boolean presente;
}