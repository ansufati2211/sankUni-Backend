package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class SeccionDTO {
    private UUID idSeccion;
    private String nombreCurso;
    private String nombreDocente;
    private String cicloAcademico;
    private Integer diaSemana; // 1 = Lunes, 7 = Domingo
    private LocalTime horaInicio;
    private LocalTime horaFin;
}