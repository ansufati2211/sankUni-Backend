package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AsistenciaRequestDTO {
    @NotNull(message = "El ID de la sección es obligatorio")
    private Long seccionId;

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "El estado de asistencia es obligatorio")
    private Boolean presente;
}