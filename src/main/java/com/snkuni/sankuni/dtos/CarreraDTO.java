package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarreraDTO {
    private Long idCarrera;
    private String tipo; // CARRERA o CURSO_CORTO
    private String nombre;
    private String descripcion;
    private String perfilAcademico;
    private String mercadoLaboral;
    private String beneficios;
    private String requisitos;
    private Boolean estado;
}