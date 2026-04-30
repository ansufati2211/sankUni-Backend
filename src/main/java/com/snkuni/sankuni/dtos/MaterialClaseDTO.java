package com.snkuni.sankuni.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialClaseDTO {
    private Long idMaterial;
    private Long idSeccion;
    private String titulo;
    private String archivoUrl;
    private LocalDateTime fechaSubida;
}