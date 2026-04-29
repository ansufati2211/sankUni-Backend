package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MatriculaRequestDTO {
    @NotNull(message = "El ID del alumno es obligatorio")
    private UUID idAlumno;

    @NotNull(message = "El ID de la sección es obligatorio")
    private UUID idSeccion;

    @NotNull(message = "El monto de pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoPago;
}