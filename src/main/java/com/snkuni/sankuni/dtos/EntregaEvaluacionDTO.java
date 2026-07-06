package com.snkuni.sankuni.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntregaEvaluacionDTO {
    private Long idEntrega;
    private Long idEvaluacion;
    private Long idAlumno;
    private String archivoUrl;
    private LocalDateTime fechaEntrega;
}
