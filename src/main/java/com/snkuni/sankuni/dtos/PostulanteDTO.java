package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostulanteDTO {
    private Long idPostulante;
    private String dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private Long carreraId;
    private String nombreCarrera;
    private String sede;
    private String turno;
    private String estado;
}