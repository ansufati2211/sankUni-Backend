package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;

@Data
@Builder
public class SeccionDTO {
    private Long idSeccion;
    private Long cursoId;    
    private Long docenteId;  
    private String nombreCurso;
    private String nombreDocente;
    private String cicloAcademico;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String modalidad;
}