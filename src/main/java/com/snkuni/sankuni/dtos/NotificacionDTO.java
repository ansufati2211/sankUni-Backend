package com.snkuni.sankuni.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NotificacionDTO {
    private Long idOrigen;
    private String tipo;
    private String titulo;
    private String desc;
    private LocalDateTime fecha;
    private String tiempo;
}