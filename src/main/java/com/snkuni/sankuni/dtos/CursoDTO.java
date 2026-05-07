package com.snkuni.sankuni.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {
    private Long idCurso;

    @NotNull(message = "Debe seleccionar una carrera")
    private Long carreraId;

    private String nombreCarrera;

    @NotBlank(message = "El nombre del curso no puede estar vacío")
    private String nombre;

    private Integer creditos; // Opcional, el Service pondrá 3 por defecto[cite: 3]

    private String temarioUrl;
    private String descripcionInformativa;
}