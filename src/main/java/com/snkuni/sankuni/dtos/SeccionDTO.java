package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeccionDTO {
    private Long idSeccion;

    @NotNull(message = "El curso es obligatorio")
    private Long cursoId;

    @NotNull(message = "El docente es obligatorio")
    private Long docenteId;

    private String nombreCurso;
    private String nombreDocente;

    @NotBlank(message = "El ciclo académico es obligatorio")
    private String cicloAcademico;

    @NotNull(message = "El día de la semana es obligatorio")
    private Integer diaSemana;

    // Cambiamos a String para evitar que Spring Boot colapse al leer el JSON
    @NotBlank(message = "La hora de inicio es obligatoria")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    private String horaFin;

    private String modalidad;
}