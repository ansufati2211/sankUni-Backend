package com.snkuni.sankuni.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertaAcademicaDTO {
    private Long idAlerta;
    private String tipo;
    private String nombreSeccion;
    private String nombreDocente;
    private String mensaje;
    private Boolean resuelta;
    private LocalDateTime fechaCreacion;
}