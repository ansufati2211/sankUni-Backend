package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuloCursoDTO {
    private Long idModulo;
    private Long idCurso;
    private String titulo;
    private String descripcion;
    private Integer orden;
}
