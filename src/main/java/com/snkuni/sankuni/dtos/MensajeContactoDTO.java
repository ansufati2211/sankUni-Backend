package com.snkuni.sankuni.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MensajeContactoDTO {
    private String nombreCompleto;
    private String correo;
    private String celular;
    private String programaInteres;
    private String mensaje;
}