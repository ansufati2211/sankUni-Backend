package com.snkuni.sankuni.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnuncioSeccionDTO {
    private Long idAnuncio;
    private Long idSeccion;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;
}
